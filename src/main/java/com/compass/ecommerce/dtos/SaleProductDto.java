package com.compass.ecommerce.dtos;

import java.util.UUID;

import com.compass.ecommerce.models.SaleProductModel;

import jakarta.validation.constraints.NotNull;

public class SaleProductDto {
    @NotNull
    private UUID id;

    @NotNull
    private Integer amount;

    @NotNull
    private UUID saleId;

    // Constructors, getters, and setters

    public SaleProductDto() {
    }

    public SaleProductDto(@NotNull UUID id, @NotNull Integer amount, @NotNull UUID saleId) {
        this.id = id;
        this.amount = amount;
        this.saleId = saleId;
    }

    public static SaleProductDto fromModel(SaleProductModel saleProductModel) {
        SaleProductDto dto = new SaleProductDto();
        dto.setId(saleProductModel.getId());
        dto.setSaleId(saleProductModel.getSale().getId()); // Assuming getSale() returns SaleModel
        return dto;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public UUID getSaleId() {
        return saleId;
    }

    public void setSaleId(UUID saleId) {
        this.saleId = saleId;
    }
}
