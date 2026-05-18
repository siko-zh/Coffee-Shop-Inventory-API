package com.zholdigaliev.coffeeshopims.dto.TransferDto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {

    @NotNull(message = "productId is required")
    private Long productId;

    @NotNull(message = "fromBranchId is required")
    private Long fromBranchId;

    @NotNull(message = "toBranchId is required")
    private Long toBranchId;

    @NotNull(message = "quantity is required")
    @DecimalMin(value = "0.01", message = "quantity must be > 0")
    private BigDecimal quantity;
}
