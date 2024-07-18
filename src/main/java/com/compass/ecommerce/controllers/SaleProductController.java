package com.compass.ecommerce.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

import com.compass.ecommerce.models.SaleProductModel;
import com.compass.ecommerce.services.SaleProductService;

@RestController
@RequestMapping("/sale-products")
public class SaleProductController {

    @Autowired
    private SaleProductService saleProductService;


    @Cacheable(value = "saleprod")
    @GetMapping
    public ResponseEntity<List<SaleProductModel>> getAllSaleProducts() {
        List<SaleProductModel> saleProducts = saleProductService.getAllSaleProducts();
        return ResponseEntity.ok(saleProducts);
    }

    @Cacheable(value = "saleProd")
    @GetMapping("/{id}")
    public ResponseEntity<SaleProductModel> getSaleProductById(@PathVariable UUID id) {
        Optional<SaleProductModel> saleProductOptional = saleProductService.getSaleProductById(id);
        return saleProductOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @CacheEvict(value = {"sale", "saleProd"}, allEntries = true)
    @PostMapping
    public ResponseEntity<SaleProductModel> createSaleProduct(@RequestBody SaleProductModel saleProduct) {
        SaleProductModel createdSaleProduct = saleProductService.createSaleProduct(saleProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSaleProduct);
    }

    @CachePut(value = {"sale", "saleProd"})
    @PutMapping("/{id}")
    public ResponseEntity<SaleProductModel> updateSaleProduct(@PathVariable UUID id, @RequestBody SaleProductModel updatedSaleProduct) {
        SaleProductModel updated = saleProductService.updateSaleProduct(id, updatedSaleProduct);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    
    @CachePut(value = {"sale", "saleProd"})
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateSaleProduct(@PathVariable UUID id) {
        saleProductService.deactivateSaleProduct(id);
        return ResponseEntity.ok("SaleProduct deactivated successfully");
    }
    
    @CachePut(value = {"sale", "saleProd"})
    @PutMapping("/{id}/reactivate")
    public ResponseEntity<String> reactivateSaleProduct(@PathVariable UUID id) {
        saleProductService.reactivateSaleProduct(id);
        return ResponseEntity.ok("SaleProduct reactivated successfully");
    }
    


}
