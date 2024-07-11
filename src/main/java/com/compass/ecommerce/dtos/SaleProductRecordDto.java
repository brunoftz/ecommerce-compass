package com.compass.ecommerce.dtos;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record SaleProductRecordDto(
        @NotNull Long id,
        @NotNull Long saleId,
        @NotNull Integer amount,
        @NotNull Double unitPrice 
) {
}
