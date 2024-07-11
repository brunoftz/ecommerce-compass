package com.compass.ecommerce.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compass.ecommerce.dtos.ProductRecordDto;
import com.compass.ecommerce.models.ProductModel;
import com.compass.ecommerce.models.SaleModel;
import com.compass.ecommerce.services.SaleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @GetMapping
    public ResponseEntity<List<SaleModel>> getAllSales() {
        List<SaleModel> sales = saleService.getAllSales();
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleModel> getSaleById(@PathVariable Long id) {
        Optional<SaleModel> saleOptional = saleService.getSaleById(id);
        return saleOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    

    @PostMapping
    public ResponseEntity<SaleModel> createSale(@RequestBody SaleModel sale) {
        SaleModel createdSale = saleService.createSale(sale);
        return ResponseEntity.status(HttpStatus.CREATED).body(saleService.createSale(sale));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleModel> updateSale(@PathVariable Long id, @RequestBody SaleModel updatedSale) {
        SaleModel updated = saleService.updateSale(id, updatedSale);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }
}
