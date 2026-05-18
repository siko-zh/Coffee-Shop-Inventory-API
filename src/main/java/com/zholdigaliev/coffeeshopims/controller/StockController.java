package com.zholdigaliev.coffeeshopims.controller;

import com.zholdigaliev.coffeeshopims.dto.StockDto.StockMinQuantityRequest;
import com.zholdigaliev.coffeeshopims.dto.StockDto.StockResponse;
import com.zholdigaliev.coffeeshopims.dto.StockDto.StockWriteOffRequest;
import com.zholdigaliev.coffeeshopims.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @GetMapping
    public ResponseEntity<List<StockResponse>> getByBranch(@RequestParam Long branchId) {
        return ResponseEntity.ok(stockService.getByBranch(branchId));
    }

    @GetMapping("/low")
    public ResponseEntity<List<StockResponse>> getLowStock(@RequestParam Long branchId) {
        return ResponseEntity.ok(stockService.getLowStock(branchId));
    }

    @PatchMapping("/{id}/min")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<StockResponse> setMinQuantity(@PathVariable Long id,
                                                         @RequestBody @Valid StockMinQuantityRequest request) {
        return ResponseEntity.ok(stockService.setMinQuantity(id, request.getMinQuantity()));
    }

    @PostMapping("/write-off")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'BARISTA')")
    public ResponseEntity<StockResponse> writeOff(@RequestBody @Valid StockWriteOffRequest request) {
        StockResponse response = stockService.writeOff(
                request.getBranchId(),
                request.getProductId(),
                request.getQuantity(),
                request.getReason()
        );
        return ResponseEntity.ok(response);
    }
}
