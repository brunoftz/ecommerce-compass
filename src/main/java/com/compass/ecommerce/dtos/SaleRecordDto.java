package com.compass.ecommerce.dtos;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record SaleRecordDto(
        @NotNull Long id,
        @NotNull Double totalPrice,
        @NotNull LocalDateTime saleDate,
        String description
) {
}
