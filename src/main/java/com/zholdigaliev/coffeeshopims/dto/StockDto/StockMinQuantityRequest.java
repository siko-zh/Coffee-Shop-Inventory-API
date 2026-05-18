package com.zholdigaliev.coffeeshopims.dto.StockDto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockMinQuantityRequest {

    @NotNull(message = "minQuantity is required")
    @DecimalMin(value = "0.0", message = "minQuantity must be >= 0")
    private BigDecimal minQuantity;
}
