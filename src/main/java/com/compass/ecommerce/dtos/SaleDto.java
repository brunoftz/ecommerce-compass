package com.compass.ecommerce.dtos;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;

public class SaleDto {
	
    @NotNull
    private LocalDateTime saleDate;

    private String description;

    @NotNull
    private List<SaleProductDto> products;

    // Constructors, getters, and setters

    public SaleDto() {
    }

    public SaleDto(@NotNull LocalDateTime saleDate, String description, @NotNull List<SaleProductDto> products) {
        this.saleDate = saleDate;
        this.description = description;
        this.products = products;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SaleProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<SaleProductDto> products) {
        this.products = products;
    }
}
