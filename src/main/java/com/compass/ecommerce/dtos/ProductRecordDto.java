package com.compass.ecommerce.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRecordDto(@NotBlank String name, @NotNull Double price, @NotNull Long stockAmount) {
}
