package com.compass.ecommerce.services;

import com.compass.ecommerce.models.SaleProductModel;
import com.compass.ecommerce.repositories.SaleProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SaleProductService {

    @Autowired
    private SaleProductRepository saleProductRepository;

    public List<SaleProductModel> getAllSaleProducts() {
        return saleProductRepository.findAll();
    }

    public Optional<SaleProductModel> getSaleProductById(Long id) {
        return saleProductRepository.findById(id);
    }

    public SaleProductModel createSaleProduct(SaleProductModel saleProduct) {
        saleProduct.setCreationDate(LocalDateTime.now());
        saleProduct.setUpdateDate(LocalDateTime.now());
        return saleProductRepository.save(saleProduct);
    }

    public SaleProductModel updateSaleProduct(Long id, SaleProductModel updatedSaleProduct) {
        Optional<SaleProductModel> saleProductOptional = saleProductRepository.findById(id);
        if (saleProductOptional.isPresent()) {
            SaleProductModel existingSaleProduct = saleProductOptional.get();
            existingSaleProduct.setSale(updatedSaleProduct.getSale());
            existingSaleProduct.setProduct(updatedSaleProduct.getProduct());
            existingSaleProduct.setAmount(updatedSaleProduct.getAmount());
            existingSaleProduct.setUnitPrice(updatedSaleProduct.getUnitPrice());
            existingSaleProduct.setUpdateDate(LocalDateTime.now());
            return saleProductRepository.save(existingSaleProduct);
        }
        return null; // Or throw an exception
    }

    public void deleteSaleProduct(Long id) {
        saleProductRepository.deleteById(id);
    }
}
