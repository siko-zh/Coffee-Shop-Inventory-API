package com.zholdigaliev.coffeeshopims.dto.StockDto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockWriteOffRequest {

    @NotNull(message = "branchId is required")
    private Long branchId;

    @NotNull(message = "productId is required")
    private Long productId;

    @NotNull(message = "quantity is required")
    @DecimalMin(value = "0.01", message = "quantity must be > 0")
    private BigDecimal quantity;

    @NotBlank(message = "reason is required")
    private String reason;
}
