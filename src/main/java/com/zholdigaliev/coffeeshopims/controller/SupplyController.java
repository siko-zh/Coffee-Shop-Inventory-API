package com.zholdigaliev.coffeeshopims.controller;

import com.zholdigaliev.coffeeshopims.dto.SupplyDto.SupplyRequest;
import com.zholdigaliev.coffeeshopims.dto.SupplyDto.SupplyResponse;
import com.zholdigaliev.coffeeshopims.service.SupplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplies")
@RequiredArgsConstructor
public class SupplyController {
    private final SupplyService supplyService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<SupplyResponse> create(@RequestBody @Valid SupplyRequest request) {
        SupplyResponse response = supplyService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SupplyResponse>> getAllByBranch(@RequestParam Long branchId) {
        return ResponseEntity.ok(supplyService.getAllByBranch(branchId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplyResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(supplyService.getById(id));
    }

    @PostMapping("/{id}/receive")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<SupplyResponse> receive(@PathVariable Long id) {
        return ResponseEntity.ok(supplyService.receive(id));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<SupplyResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(supplyService.cancel(id));
    }
}
