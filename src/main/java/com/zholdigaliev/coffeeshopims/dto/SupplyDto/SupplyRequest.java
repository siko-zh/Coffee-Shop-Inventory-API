package com.zholdigaliev.coffeeshopims.dto.SupplyDto;

import com.zholdigaliev.coffeeshopims.dto.SupplyItemDto.SupplyItemRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SupplyRequest {

    @NotNull(message = "supplierId is required")
    private Long supplierId;

    @NotNull(message = "branchId is required")
    private Long branchId;

    @NotEmpty(message = "items must not be empty")
    @Valid
    private List<SupplyItemRequest> items;

    private LocalDateTime supplyDate;
}
