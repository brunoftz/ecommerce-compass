package com.compass.ecommerce.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compass.ecommerce.models.SaleProductModel;
import com.compass.ecommerce.repositories.SaleProductRepository;

@Service
public class SaleProductService {

    @Autowired
    private SaleProductRepository saleProductRepository;

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
}
