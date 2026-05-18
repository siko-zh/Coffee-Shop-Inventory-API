package com.zholdigaliev.coffeeshopims.service.impl;

import com.zholdigaliev.coffeeshopims.dto.SupplyDto.SupplyRequest;
import com.zholdigaliev.coffeeshopims.dto.SupplyDto.SupplyResponse;
import com.zholdigaliev.coffeeshopims.dto.SupplyItemDto.SupplyItemRequest;
import com.zholdigaliev.coffeeshopims.dto.mapper.SupplyItemMapper;
import com.zholdigaliev.coffeeshopims.dto.mapper.SupplyMapper;
import com.zholdigaliev.coffeeshopims.entity.*;
import com.zholdigaliev.coffeeshopims.repository.*;
import com.zholdigaliev.coffeeshopims.security.CustomUserDetails;
import com.zholdigaliev.coffeeshopims.service.SupplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplyServiceImpl implements SupplyService {
    private final SupplyRepository supplyRepository;
    private final SupplierRepository supplierRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;
    private final SupplyItemRepository supplyItemRepository;
    private final StockRepository stockRepository;
    private final StockMovementRepository stockMovementRepository;
    private final UserRepository userRepository;
    private final SupplyMapper mapper;
    private final SupplyItemMapper supplyItemMapper;

    @Override
    public SupplyResponse create(SupplyRequest request) {
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found: " + request.getSupplierId()));
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found: " + request.getBranchId()));

        Supply supply = mapper.toEntity(request);
        supply.setSupplier(supplier);
        supply.setBranch(branch);
        supply.setCreatedBy(getCurrentUser());
        supply.setStatus(SupplyStatus.PENDING);

        Supply savedSupply = supplyRepository.save(supply);

        List<SupplyItem> items = new ArrayList<>();
        Set<Long> productIds = request.getItems().stream()
                .map(SupplyItemRequest::getProductId)
                .collect(Collectors.toSet());

        if (productIds.size() != request.getItems().size()) {
            throw new RuntimeException("The supply must not contain duplicate items");
        }

        List<Product> products = productRepository.findAllById(productIds);

        if (products.size() != productIds.size()) {
            throw new RuntimeException("One or more products were not found in the database");
        }

        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        for (SupplyItemRequest item : request.getItems()) {
            SupplyItem supplyItem = supplyItemMapper.toEntity(item);
            Product product = productMap.get(item.getProductId());
            supplyItem.setProduct(product);
            supplyItem.setSupply(savedSupply);
            items.add(supplyItem);
        }
        supplyItemRepository.saveAll(items);

        savedSupply.setItems(items);

        return mapper.toResponse(savedSupply);
    }

    @Override
    public SupplyResponse getById(Long id) {
        Supply supply = supplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supply not found: " + id));
        return mapper.toResponse(supply);
    }

    @Override
    public List<SupplyResponse> getAllByBranch(Long branchId) {
        return supplyRepository.findAllByBranchId(branchId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public SupplyResponse receive(Long id) {
        Supply supply = supplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supply not found: " + id));

        if (!supply.getStatus().equals(SupplyStatus.PENDING)) {
            throw new RuntimeException("Supply is already accepted or cancelled");
        }

        if (supply.getItems() == null || supply.getItems().isEmpty()) {
            throw new RuntimeException("An empty supply cannot be accepted");
        }

        for (SupplyItem item : supply.getItems()) {
            Stock stock = stockRepository.findStockByBranchAndProduct(supply.getBranch(), item.getProduct())
                    .orElseGet(() -> {
                        Stock newStock = new Stock();
                        newStock.setProduct(item.getProduct());
                        newStock.setBranch(supply.getBranch());
                        newStock.setQuantity(BigDecimal.ZERO);
                        newStock.setMinQuantity(BigDecimal.ZERO);
                        return newStock;
                    });

            stock.setQuantity(stock.getQuantity().add(item.getQuantity()));
            stockRepository.save(stock);

            StockMovement movement = new StockMovement();
            movement.setProduct(item.getProduct());
            movement.setBranch(supply.getBranch());
            movement.setCreatedBy(getCurrentUser());
            movement.setType(MovementType.IN);
            movement.setQuantity(item.getQuantity());
            movement.setReason("Supply #" + supply.getId() + " received");
            stockMovementRepository.save(movement);
        }

        supply.setStatus(SupplyStatus.RECEIVED);
        supply.setReceivedAt(LocalDateTime.now());
        Supply savedSupply = supplyRepository.save(supply);

        return mapper.toResponse(savedSupply);
    }

    @Override
    public SupplyResponse cancel(Long id) {
        Supply supply = supplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supply not found: " + id));

        if (supply.getStatus().equals(SupplyStatus.RECEIVED)) {
            throw new RuntimeException("A received supply cannot be cancelled");
        }

        supply.setStatus(SupplyStatus.CANCELLED);
        Supply saved = supplyRepository.save(supply);

        return mapper.toResponse(saved);
    }

    private User getCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Current user not found"));
    }
}
