package com.zholdigaliev.coffeeshopims.controller;

import com.zholdigaliev.coffeeshopims.dto.ShiftDto.ShiftRequest;
import com.zholdigaliev.coffeeshopims.dto.ShiftDto.ShiftResponse;
import com.zholdigaliev.coffeeshopims.dto.StockMovementDto.StockMovementResponse;
import com.zholdigaliev.coffeeshopims.service.ShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shifts")
@RequiredArgsConstructor
public class ShiftController {
    private final ShiftService shiftService;

    @PostMapping("/open")
    public ResponseEntity<ShiftResponse> open(@RequestParam Long userId,
                                               @RequestBody @Valid ShiftRequest request) {
        ShiftResponse response = shiftService.openedByUser(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<ShiftResponse> close(@PathVariable Long id,
                                                @RequestBody Map<String, Long> body) {
        Long userId = body.get("userId");
        return ResponseEntity.ok(shiftService.closedByUser(id, userId));
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
