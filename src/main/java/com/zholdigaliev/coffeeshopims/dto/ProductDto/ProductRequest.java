package com.zholdigaliev.coffeeshopims.dto.ProductDto;

import com.zholdigaliev.coffeeshopims.entity.Unit;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotBlank(message = "name is required")
    private String name;

    private String description;

    @NotNull(message = "unit is required")
    private Unit unit;

    @NotNull(message = "price is required")
    @DecimalMin(value = "0.01", message = "price must be > 0")
    private BigDecimal price;

    @NotNull(message = "categoryId is required")
    private Long categoryId;

    private Long supplierId;
}
