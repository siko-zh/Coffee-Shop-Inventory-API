package com.zholdigaliev.coffeeshopims.dto.ShiftDto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShiftCloseRequest {

    @NotNull(message = "userId is required")
    private Long userId;
}
