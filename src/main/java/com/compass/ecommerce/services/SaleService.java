package com.compass.ecommerce.services;

import com.compass.ecommerce.models.SaleModel;
import com.compass.ecommerce.repositories.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    public List<SaleModel> getAllSales() {
        return saleRepository.findAll();
    }

    public Optional<SaleModel> getSaleById(Long id) {
        return saleRepository.findById(id);
    }

    public SaleModel createSale(SaleModel sale) {
        sale.setCreationDate(LocalDateTime.now());
        sale.setUpdateDate(LocalDateTime.now());
        return saleRepository.save(sale);
    }

    public SaleModel updateSale(Long id, SaleModel updatedSale) {
        Optional<SaleModel> saleOptional = saleRepository.findById(id);
        if (saleOptional.isPresent()) {
            SaleModel existingSale = saleOptional.get();
            existingSale.setTotalPrice(updatedSale.getTotalPrice());
            existingSale.setSaleDate(updatedSale.getSaleDate());
            existingSale.setDescription(updatedSale.getDescription());
            existingSale.setUpdateDate(LocalDateTime.now());
            return saleRepository.save(existingSale);
        }
        return null; // Or throw an exception
    }

    public void deleteSale(Long id) {
        saleRepository.deleteById(id);
    }
}
