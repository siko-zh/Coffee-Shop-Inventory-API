package com.zholdigaliev.coffeeshopims.dto.ShiftDto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShiftRequest {

    @NotNull(message = "branchId is required")
    private Long branchId;

    private String notes;
}
