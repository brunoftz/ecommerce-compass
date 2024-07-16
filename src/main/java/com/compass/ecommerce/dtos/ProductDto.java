package com.compass.ecommerce.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductDto {
    @NotBlank
    private String name;

    @NotNull
    private Double price;

    @NotNull
    private Long stockAmount;

    // Constructors, getters, and setters

    public ProductDto() {
    }

    public ProductDto(@NotBlank String name, @NotNull Double price, @NotNull Long stockAmount) {
        this.name = name;
        this.price = price;
        this.stockAmount = stockAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(Long stockAmount) {
        this.stockAmount = stockAmount;
    }
}
