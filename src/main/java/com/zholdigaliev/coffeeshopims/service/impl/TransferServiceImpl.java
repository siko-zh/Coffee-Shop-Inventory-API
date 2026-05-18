package com.zholdigaliev.coffeeshopims.service.impl;

import com.zholdigaliev.coffeeshopims.dto.TransferDto.TransferRequest;
import com.zholdigaliev.coffeeshopims.dto.TransferDto.TransferResponse;
import com.zholdigaliev.coffeeshopims.dto.mapper.TransferMapper;
import com.zholdigaliev.coffeeshopims.entity.*;
import com.zholdigaliev.coffeeshopims.repository.*;
import com.zholdigaliev.coffeeshopims.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TransferServiceImpl implements TransferService {
    private final TransferRepository transferRepository;
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final StockMovementRepository stockMovementRepository;

    private final TransferMapper mapper;


    @Override
    public TransferResponse create(TransferRequest request, Long userId) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + request.getProductId()));
        Branch branchFrom = branchRepository.findById(request.getFromBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found: " + request.getFromBranchId()));
        Branch branchTo = branchRepository.findById(request.getToBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found: " + request.getToBranchId()));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        Stock stock = stockRepository.findStockByBranchAndProduct(branchFrom, product)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        if(!(stock.getQuantity().compareTo(request.getQuantity()) >= 0)) {
            throw new RuntimeException("There is not enough stock at the shipping branch");
        }

        Transfer transfer = mapper.toEntity(request);

        transfer.setCreatedBy(user);
        transfer.setProduct(product);
        transfer.setFromBranch(branchFrom);
        transfer.setToBranch(branchTo);
        transfer.setStatus(TransferStatus.PENDING);

        Transfer saved = transferRepository.save(transfer);

        return mapper.toResponse(saved);
    }

    @Override
    public TransferResponse complete(Long transferId) {
        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new RuntimeException("Transfer is not found: " + transferId));

        if(!transfer.getStatus().equals(TransferStatus.PENDING)) {
            throw new RuntimeException("Transfer is already completed or canceled");
        }

        Stock stockFrom = stockRepository.findStockByBranchAndProduct(transfer.getFromBranch(), transfer.getProduct())
                .orElseThrow(() -> new RuntimeException("Stock is not found"));
        Stock stockTo = stockRepository.findStockByBranchAndProduct(transfer.getToBranch(), transfer.getProduct())
                .orElse(new Stock(null, transfer.getProduct(), transfer.getToBranch(), BigDecimal.ZERO, null, null));

        if(!(stockFrom.getQuantity().compareTo(transfer.getQuantity()) >= 0)) {
            throw new RuntimeException("There is not enough stock at the shipping branch");
        }

        stockFrom.setQuantity(stockFrom.getQuantity().subtract(transfer.getQuantity()));
        stockTo.setQuantity(stockTo.getQuantity().add(transfer.getQuantity()));

        stockRepository.save(stockFrom);
        stockRepository.save(stockTo);

        StockMovement stockMovementOUT = new StockMovement();
        StockMovement stockMovementIN = new StockMovement();

        stockMovementOUT.setType(MovementType.TRANSFER_OUT);
        stockMovementOUT.setBranch(transfer.getFromBranch());
        stockMovementOUT.setReason("Transfer to branch: " + transfer.getToBranch().getName() + " product: " + transfer.getProduct().getName());
        stockMovementOUT.setCreatedBy(transfer.getCreatedBy());
        stockMovementOUT.setProduct(transfer.getProduct());
        stockMovementOUT.setQuantity(transfer.getQuantity());

        stockMovementRepository.save(stockMovementOUT);

        stockMovementIN.setType(MovementType.TRANSFER_IN);
        stockMovementIN.setBranch(transfer.getToBranch());
        stockMovementIN.setReason("Transfer from branch: " + transfer.getFromBranch().getName() + " product: " + transfer.getProduct().getName());
        stockMovementIN.setCreatedBy(transfer.getCreatedBy());
        stockMovementIN.setProduct(transfer.getProduct());
        stockMovementIN.setQuantity(transfer.getQuantity());

        stockMovementRepository.save(stockMovementIN);

        transfer.setStatus(TransferStatus.COMPLETED);
        transfer.setCompletedAt(LocalDateTime.now());

        Transfer saved = transferRepository.save(transfer);

        return mapper.toResponse(saved);
    }

    @Override
    public TransferResponse cancel(Long transferId) {
        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new RuntimeException("Transfer is not found: " + transferId));

        if(transfer.getStatus().equals(TransferStatus.COMPLETED)) {
            throw new RuntimeException("Can not cancel completed transfer!");
        }

        transfer.setStatus(TransferStatus.CANCELLED);

        Transfer saved = transferRepository.save(transfer);

        return mapper.toResponse(saved);
    }

    @Override
    public TransferResponse getById(Long transferId) {
        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new RuntimeException("Transfer is not found: " + transferId));

        return mapper.toResponse(transfer);
    }

    @Override
    public List<TransferResponse> getAllByBranch(Long branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found: " + branchId));

        return transferRepository.findAllByFromBranchOrToBranch(branch, branch)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
