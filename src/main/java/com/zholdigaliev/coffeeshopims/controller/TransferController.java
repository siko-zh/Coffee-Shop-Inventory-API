package com.zholdigaliev.coffeeshopims.controller;

import com.zholdigaliev.coffeeshopims.dto.TransferDto.TransferRequest;
import com.zholdigaliev.coffeeshopims.dto.TransferDto.TransferResponse;
import com.zholdigaliev.coffeeshopims.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TransferResponse> create(@RequestBody @Valid TransferRequest request,
                                                    @RequestParam Long userId) {
        TransferResponse response = transferService.create(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TransferResponse>> getAllByBranch(@RequestParam Long branchId) {
        return ResponseEntity.ok(transferService.getAllByBranch(branchId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(transferService.getById(id));
    }

    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TransferResponse> complete(@PathVariable Long id) {
        return ResponseEntity.ok(transferService.complete(id));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TransferResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(transferService.cancel(id));
    }
}
