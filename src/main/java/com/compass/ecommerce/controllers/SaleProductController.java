package com.compass.ecommerce.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.compass.ecommerce.dtos.SaleProductDto;
import com.compass.ecommerce.models.SaleProductModel;
import com.compass.ecommerce.services.SaleProductService;

@RestController
@RequestMapping("/api/sale-products")
public class SaleProductController {

    @Autowired
    private SaleProductService saleProductService;

    @GetMapping
    public ResponseEntity<List<SaleProductDto>> getAllSaleProducts() {
        List<SaleProductModel> saleProducts = saleProductService.getAllSaleProducts();
        List<SaleProductDto> dtos = saleProducts.stream()
                .map(SaleProductDto::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleProductDto> getSaleProductById(@PathVariable UUID id) {
        Optional<SaleProductModel> saleProductOptional = saleProductService.getSaleProductById(id);
        return saleProductOptional.map(saleProduct -> ResponseEntity.ok(SaleProductDto.fromModel(saleProduct)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SaleProductDto> createSaleProduct(@RequestBody SaleProductModel saleProduct) {
        SaleProductModel createdSaleProduct = saleProductService.createSaleProduct(saleProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(SaleProductDto.fromModel(createdSaleProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleProductDto> updateSaleProduct(@PathVariable UUID id, @RequestBody SaleProductModel updatedSaleProduct) {
        SaleProductModel updated = saleProductService.updateSaleProduct(id, updatedSaleProduct);
        return updated != null ? ResponseEntity.ok(SaleProductDto.fromModel(updated)) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSaleProduct(@PathVariable UUID id) {
        saleProductService.deleteSaleProduct(id);
        return ResponseEntity.noContent().build();
    }
}
