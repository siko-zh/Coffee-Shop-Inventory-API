package com.zholdigaliev.coffeeshopims.controller;

import com.zholdigaliev.coffeeshopims.dto.StockDto.StockResponse;
import com.zholdigaliev.coffeeshopims.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<StockResponse> setMinQuantity(@PathVariable Long id,
                                                         @RequestBody Map<String, BigDecimal> body) {
        return ResponseEntity.ok(stockService.setMinQuantity(id, body.get("minQuantity")));
    }

    @PostMapping("/write-off")
    public ResponseEntity<StockResponse> writeOff(@RequestBody Map<String, Object> body) {
        Long branchId = Long.valueOf(body.get("branchId").toString());
        Long productId = Long.valueOf(body.get("productId").toString());
        BigDecimal quantity = new BigDecimal(body.get("quantity").toString());
        String reason = (String) body.get("reason");

        StockResponse response = stockService.writeOff(branchId, productId, quantity, reason);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
