package com.zholdigaliev.coffeeshopims.controller;


import com.zholdigaliev.coffeeshopims.dto.ProductDto.ProductRequest;
import com.zholdigaliev.coffeeshopims.dto.ProductDto.ProductResponse;
import com.zholdigaliev.coffeeshopims.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping()
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @GetMapping()
    public ResponseEntity<List<ProductResponse>> getAllByCategory(@RequestParam(value = "category", required = false) Long categoryId) {
        return ResponseEntity.ok(productService.getAllByCategoryId(categoryId));
    }

    @PostMapping()
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest request) {
        ProductResponse response = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

