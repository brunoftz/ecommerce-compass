package com.compass.ecommerce.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.compass.ecommerce.dtos.ProductRecordDto;
import com.compass.ecommerce.models.ProductModel;
import com.compass.ecommerce.services.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductModel> addProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(productRecordDto));
    }

    @GetMapping
    public ResponseEntity<List<ProductModel>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id) {
        return productService.getOneProduct(id)
                .<ResponseEntity<Object>>map(product -> ResponseEntity.status(HttpStatus.OK).body(product))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid ProductRecordDto productRecordDto) {
        return productService.updateProduct(id, productRecordDto)
                .<ResponseEntity<Object>>map(product -> ResponseEntity.status(HttpStatus.OK).body(product))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
        return productService.deleteProduct(id)
                .<ResponseEntity<Object>>map(product -> ResponseEntity.status(HttpStatus.OK).body("Produto deletado com sucesso."))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado."));
    }
}
