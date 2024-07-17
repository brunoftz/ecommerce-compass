package com.compass.ecommerce.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compass.ecommerce.dtos.SaleProductDto;
import com.compass.ecommerce.models.ProductModel;
import com.compass.ecommerce.models.SaleModel;
import com.compass.ecommerce.models.SaleProductModel;
import com.compass.ecommerce.repositories.ProductRepository;
import com.compass.ecommerce.repositories.SaleProductRepository;
import com.compass.ecommerce.repositories.SaleRepository;

import jakarta.transaction.Transactional;

@Service
public class SaleProductService {

	@Autowired
	private SaleProductRepository saleProductRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private SaleRepository saleRepository;

	public List<SaleProductModel> getAllSaleProducts() {
		return saleProductRepository.findAll();
	}

	public Optional<SaleProductModel> getSaleProductById(UUID id) {
		return saleProductRepository.findById(id);
	}

	public SaleProductModel createSaleProduct(SaleProductModel saleProduct) {
		saleProduct.setCreationDate(LocalDateTime.now());
		saleProduct.setUpdateDate(null); // No update date on creation
		return saleProductRepository.save(saleProduct);
	}

	public SaleProductModel updateSaleProduct(UUID id, SaleProductModel updatedSaleProduct) {
		Optional<SaleProductModel> saleProductOptional = saleProductRepository.findById(id);
		if (saleProductOptional.isPresent()) {
			SaleProductModel existingSaleProduct = saleProductOptional.get();
			existingSaleProduct.setSale(updatedSaleProduct.getSale());
			existingSaleProduct.setProduct(updatedSaleProduct.getProduct());
			existingSaleProduct.setAmount(updatedSaleProduct.getAmount());
			// existingSaleProduct.setUnitPrice(updatedSaleProduct.getUnitPrice());
			existingSaleProduct.setUpdateDate(LocalDateTime.now());
			return saleProductRepository.save(existingSaleProduct);
		}
		return null; // Or throw an exception
	}

	public void deleteSaleProduct(UUID id) {
		saleProductRepository.deleteById(id);
	}

	@Transactional
	public void deactivateSaleProduct(UUID saleProductId) {
		SaleProductModel saleProduct = saleProductRepository.findById(saleProductId)
				.orElseThrow(() -> new RuntimeException("SaleProduct not found with id: " + saleProductId));
		if (saleProduct.getStatus() != null && !saleProduct.getStatus()) {
			throw new RuntimeException("SaleProduct is already inactive");
		}
		saleProduct.setStatus(false);

		// Atualiza o estoque do produto
		ProductModel product = saleProduct.getProduct();
		product.setStockAmount(product.getStockAmount() + saleProduct.getAmount());
		productRepository.save(product);

		saleProductRepository.save(saleProduct);

		// Recalcular o valor total da venda
		recalculateSaleTotalPrice(saleProduct.getSale());
	}

	@Transactional
	public void reactivateSaleProduct(UUID saleProductId) {
		SaleProductModel saleProduct = saleProductRepository.findById(saleProductId)
				.orElseThrow(() -> new RuntimeException("SaleProduct not found with id: " + saleProductId));
		if (saleProduct.getStatus() != null && saleProduct.getStatus()) {
			throw new RuntimeException("SaleProduct is already active");
		}

		if (!saleProduct.getStatus()) {
			// Verifica se há estoque suficiente
			ProductModel product = saleProduct.getProduct();
			if (product.getStockAmount() < saleProduct.getAmount()) {
				throw new RuntimeException("Insufficient stock to reactivate product: " + product.getName());
			}
			saleProduct.setStatus(true);

			// Atualiza o estoque do produto
			product.setStockAmount(product.getStockAmount() - saleProduct.getAmount());
			productRepository.save(product);

			saleProductRepository.save(saleProduct);

			// Recalcular o valor total da venda
			recalculateSaleTotalPrice(saleProduct.getSale());
		}
	}
	
	
	@Transactional
    public SaleProductModel addProductToSale(UUID saleId, SaleProductDto saleProductDto) {
        SaleModel sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found with id: " + saleId));

        ProductModel product = productRepository.findById(saleProductDto.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

     // Check if the product is already added to the sale
        boolean productAlreadyAdded = sale.getSaleProducts().stream()
                .anyMatch(sp -> sp.getProduct().getId().equals(product.getId()));

        if (productAlreadyAdded) {
            throw new RuntimeException("Product " + product.getName() + " is already added to the sale.");
        }
        
        if (product.getStockAmount() < saleProductDto.getAmount()) {
            throw new RuntimeException("Insufficient stock to add product: " + product.getName());
        }

        SaleProductModel saleProductModel = new SaleProductModel();
        saleProductModel.setSale(sale);
        saleProductModel.setProduct(product);
        saleProductModel.setAmount(saleProductDto.getAmount());
        saleProductModel.setStatus(true);

        // Atualiza o estoque do produto
        product.setStockAmount(product.getStockAmount() - saleProductModel.getAmount());
        productRepository.save(product);

        sale.getSaleProducts().add(saleProductModel);
        saleProductRepository.save(saleProductModel);

        // Recalcula o totalPrice da venda
        recalculateSaleTotalPrice(sale);

        return saleProductModel;
    }

	private void recalculateSaleTotalPrice(SaleModel sale) {
		double totalPrice = sale.getSaleProducts().stream().filter(SaleProductModel::getStatus) // Apenas produtos
																								// ativos
				.mapToDouble(sp -> sp.getProduct().getPrice() * sp.getAmount()).sum();
		sale.setTotalPrice(totalPrice);
		saleRepository.save(sale);
	}
}
