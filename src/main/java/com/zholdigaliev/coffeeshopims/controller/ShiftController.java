package com.zholdigaliev.coffeeshopims.controller;

import com.zholdigaliev.coffeeshopims.dto.ShiftDto.ShiftCloseRequest;
import com.zholdigaliev.coffeeshopims.dto.ShiftDto.ShiftRequest;
import com.zholdigaliev.coffeeshopims.dto.ShiftDto.ShiftResponse;
import com.zholdigaliev.coffeeshopims.dto.StockMovementDto.StockMovementResponse;
import com.zholdigaliev.coffeeshopims.service.ShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shifts")
@RequiredArgsConstructor
public class ShiftController {
    private final ShiftService shiftService;

    @PostMapping("/open")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'BARISTA')")
    public ResponseEntity<ShiftResponse> open(@RequestParam Long userId,
                                               @RequestBody @Valid ShiftRequest request) {
        ShiftResponse response = shiftService.openedByUser(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/close")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'BARISTA')")
    public ResponseEntity<ShiftResponse> close(@PathVariable Long id,
                                                @RequestBody @Valid ShiftCloseRequest request) {
        return ResponseEntity.ok(shiftService.closedByUser(id, request.getUserId()));
    }

    @GetMapping("/active")
    public ResponseEntity<ShiftResponse> getActive(@RequestParam Long branchId) {
        return ResponseEntity.ok(shiftService.getActiveByBranch(branchId));
    }

    @GetMapping
    public ResponseEntity<List<ShiftResponse>> getAllByBranch(@RequestParam Long branchId) {
        return ResponseEntity.ok(shiftService.getAllByBranch(branchId));
    }

    @GetMapping("/{id}/movements")
    public ResponseEntity<List<StockMovementResponse>> getMovements(@PathVariable Long id) {
        return ResponseEntity.ok(shiftService.getMovementsByShift(id));
    }
}
