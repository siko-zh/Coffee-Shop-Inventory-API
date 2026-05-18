package com.zholdigaliev.coffeeshopims.service.impl;

import com.zholdigaliev.coffeeshopims.dto.ProductDto.ProductRequest;
import com.zholdigaliev.coffeeshopims.dto.ProductDto.ProductResponse;
import com.zholdigaliev.coffeeshopims.dto.mapper.BranchMapper;
import com.zholdigaliev.coffeeshopims.dto.mapper.CategoryMapper;
import com.zholdigaliev.coffeeshopims.dto.mapper.ProductMapper;
import com.zholdigaliev.coffeeshopims.entity.Category;
import com.zholdigaliev.coffeeshopims.entity.Product;
import com.zholdigaliev.coffeeshopims.entity.Supplier;
import com.zholdigaliev.coffeeshopims.repository.CategoryRepository;
import com.zholdigaliev.coffeeshopims.repository.ProductRepository;
import com.zholdigaliev.coffeeshopims.repository.SupplierRepository;
import com.zholdigaliev.coffeeshopims.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final ProductMapper mapper;

    @Override
    public ProductResponse create(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found: " + request.getCategoryId()));
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found: " + request.getSupplierId()));
        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setUnit(request.getUnit());
        product.setPrice(request.getPrice());
        product.setCategory(category);
        product.setSupplier(supplier);

        Product saved = productRepository.save(product);

        return mapper.toResponse(saved);
    }

    @Override
    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getAllByCategoryId(Long categoryId) {
        return productRepository.findAllByCategoryId(categoryId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
        return mapper.toResponse(product);
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found: " + request.getCategoryId()));
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found: " + request.getSupplierId()));
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setUnit(request.getUnit());
        product.setPrice(request.getPrice());
        product.setCategory(category);
        product.setSupplier(supplier);

        Product saved = productRepository.save(product);

        return mapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));

        productRepository.delete(product);
    }
}
