package com.zholdigaliev.coffeeshopims.dto.StockMovementDto;

import com.zholdigaliev.coffeeshopims.entity.MovementType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class StockMovementFilter {
    private Long productId;
    private Long branchId;
    private MovementType type;
    private Long userId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;
}
