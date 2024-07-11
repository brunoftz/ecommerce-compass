package com.compass.ecommerce.controllers;

import com.compass.ecommerce.models.SaleProductModel;
import com.compass.ecommerce.services.SaleProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sale-products")
public class SaleProductController {

    @Autowired
    private SaleProductService saleProductService;

    @GetMapping
    public ResponseEntity<List<SaleProductModel>> getAllSaleProducts() {
        List<SaleProductModel> saleProducts = saleProductService.getAllSaleProducts();
        return ResponseEntity.ok(saleProducts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleProductModel> getSaleProductById(@PathVariable Long id) {
        Optional<SaleProductModel> saleProductOptional = saleProductService.getSaleProductById(id);
        return saleProductOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SaleProductModel> createSaleProduct(@RequestBody SaleProductModel saleProduct) {
        SaleProductModel createdSaleProduct = saleProductService.createSaleProduct(saleProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSaleProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleProductModel> updateSaleProduct(@PathVariable Long id, @RequestBody SaleProductModel updatedSaleProduct) {
        SaleProductModel updated = saleProductService.updateSaleProduct(id, updatedSaleProduct);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSaleProduct(@PathVariable Long id) {
        saleProductService.deleteSaleProduct(id);
        return ResponseEntity.noContent().build();
    }
}
