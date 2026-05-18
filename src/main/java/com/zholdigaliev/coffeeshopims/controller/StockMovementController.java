package com.zholdigaliev.coffeeshopims.controller;

import com.zholdigaliev.coffeeshopims.dto.StockMovementDto.StockMovementResponse;
import com.zholdigaliev.coffeeshopims.service.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/movements")
@RequiredArgsConstructor
public class StockMovementController {
    private final StockMovementService stockMovementService;

    @GetMapping
    public ResponseEntity<Page<StockMovementResponse>> getAll(Pageable pageable,
                                                               @RequestParam(required = false) Long branchId) {
        if (branchId != null) {
            return ResponseEntity.ok(stockMovementService.getByBranch(branchId, pageable));
        }
        return ResponseEntity.ok(stockMovementService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockMovementResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(stockMovementService.getById(id));
    }

    @GetMapping("/timerange")
    public ResponseEntity<List<StockMovementResponse>> getByTimeRange(
            @RequestParam Long branchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(stockMovementService.getByTimeRange(branchId, from, to));
    }
}
