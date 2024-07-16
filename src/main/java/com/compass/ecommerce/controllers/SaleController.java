package com.compass.ecommerce.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compass.ecommerce.dtos.SaleDto;
import com.compass.ecommerce.models.SaleModel;
import com.compass.ecommerce.services.SaleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @PostMapping
    public ResponseEntity<SaleModel> createSale(@RequestBody @Valid SaleDto saleDto) {
        SaleModel createdSale = saleService.createSale(saleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSale);
    }
    
    @GetMapping
    public ResponseEntity<List<SaleModel>> getAllSales() {
        return ResponseEntity.status(HttpStatus.OK).body(saleService.getAllSales());
    }
}
