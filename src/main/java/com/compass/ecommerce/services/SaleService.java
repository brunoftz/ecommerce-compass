package com.compass.ecommerce.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compass.ecommerce.dtos.SaleDto;
import com.compass.ecommerce.dtos.SaleProductDto;
import com.compass.ecommerce.models.ProductModel;
import com.compass.ecommerce.models.SaleModel;
import com.compass.ecommerce.models.SaleProductModel;
import com.compass.ecommerce.repositories.ProductRepository;
import com.compass.ecommerce.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository saleRepository;

	@Autowired
	private ProductRepository productRepository;

	@Transactional
	public SaleModel createSale(SaleDto saleDto) {
		SaleModel saleModel = new SaleModel();
		saleModel.setSaleDate(saleDto.getSaleDate());
		saleModel.setDescription(saleDto.getDescription());

		double totalPrice = 0.0;
		for (SaleProductDto saleProductDto : saleDto.getProducts()) {
			ProductModel product = productRepository.findById(saleProductDto.getId())
					.orElseThrow(() -> new RuntimeException("Product not found"));

			// Verifica se há estoque suficiente
			if (product.getStockAmount() < saleProductDto.getAmount()) {
				throw new RuntimeException("Insufficient stock for product: " + product.getName());
			}

			// Atualiza o estoque do produto
			product.setStockAmount(product.getStockAmount() - saleProductDto.getAmount());
			productRepository.save(product);

			SaleProductModel saleProductModel = new SaleProductModel();
			saleProductModel.setSale(saleModel);
			saleProductModel.setProduct(product);
			saleProductModel.setAmount(saleProductDto.getAmount());
			totalPrice += product.getPrice() * saleProductDto.getAmount();
			saleModel.getSaleProducts().add(saleProductModel);
		}

		saleModel.setTotalPrice(totalPrice);
		return saleRepository.save(saleModel);
	}

	public List<SaleModel> getAllSales() {
		return saleRepository.findAll();
	}

	@Transactional
	public void deleteSaleById(UUID saleId) {
		Optional<SaleModel> saleOptional = saleRepository.findById(saleId);
		if (saleOptional.isPresent()) {
			SaleModel sale = saleOptional.get();
			saleRepository.delete(sale);
		} else {
			throw new RuntimeException("Sale not found with id: " + saleId);
		}
	}

	@Transactional
	public SaleModel updateSale(UUID saleId, SaleDto saleDto) {
		Optional<SaleModel> saleOptional = saleRepository.findById(saleId);
		if (saleOptional.isPresent()) {
			SaleModel saleModel = saleOptional.get();

			// Atualiza os dados da venda com base no SaleDto recebido
			saleModel.setSaleDate(saleDto.getSaleDate());
			saleModel.setDescription(saleDto.getDescription());

			// Limpa os produtos da venda para recriá-los com os novos dados
			saleModel.getSaleProducts().clear();

			double totalPrice = 0.0;
			for (SaleProductDto saleProductDto : saleDto.getProducts()) {
				ProductModel product = productRepository.findById(saleProductDto.getId())
						.orElseThrow(() -> new RuntimeException("Product not found"));
				SaleProductModel saleProductModel = new SaleProductModel();
				saleProductModel.setSale(saleModel);
				saleProductModel.setProduct(product);
				saleProductModel.setAmount(saleProductDto.getAmount());
				totalPrice += product.getPrice() * saleProductDto.getAmount();
				saleModel.getSaleProducts().add(saleProductModel);
			}

			saleModel.setTotalPrice(totalPrice);
			return saleRepository.save(saleModel);
		} else {
			throw new RuntimeException("Sale not found with id: " + saleId);
		}
	}
}
