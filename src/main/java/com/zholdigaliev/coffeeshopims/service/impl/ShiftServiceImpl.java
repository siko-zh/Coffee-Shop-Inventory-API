package com.zholdigaliev.coffeeshopims.service.impl;

import com.zholdigaliev.coffeeshopims.dto.ShiftDto.ShiftRequest;
import com.zholdigaliev.coffeeshopims.dto.ShiftDto.ShiftResponse;
import com.zholdigaliev.coffeeshopims.dto.StockMovementDto.StockMovementResponse;
import com.zholdigaliev.coffeeshopims.dto.mapper.ShiftMapper;
import com.zholdigaliev.coffeeshopims.dto.mapper.StockMovementMapper;
import com.zholdigaliev.coffeeshopims.entity.*;
import com.zholdigaliev.coffeeshopims.repository.BranchRepository;
import com.zholdigaliev.coffeeshopims.repository.ShiftRepository;
import com.zholdigaliev.coffeeshopims.repository.StockMovementRepository;
import com.zholdigaliev.coffeeshopims.repository.UserRepository;
import com.zholdigaliev.coffeeshopims.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {
    private final ShiftRepository shiftRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final StockMovementRepository stockMovementRepository;
    private final StockMovementMapper stockMovementMapper;
    private final ShiftMapper mapper;

    @Override
    public ShiftResponse openedByUser(Long userId, ShiftRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found: " + request.getBranchId()));

        Shift shift = shiftRepository.findByBranch(branch)
                .orElse(new Shift());

        if(shift.getStatus().equals(ShiftStatus.OPEN)) {
            throw new RuntimeException("Shift is already opened");
        }

        Shift newShift = shift;

        newShift.setBranch(branch);
        newShift.setOpenedBy(user);
        newShift.setStatus(ShiftStatus.OPEN);

        Shift saved = shiftRepository.save(newShift);

        return mapper.toResponse(saved);
    }

    @Override
    public ShiftResponse closedByUser(Long id, Long userId) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift not found: " + id));

        if(!shift.getStatus().equals(ShiftStatus.OPEN)) {
            throw new RuntimeException("Shift is already closed");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        shift.setStatus(ShiftStatus.CLOSED);
        shift.setClosedBy(user);
        shift.setClosedAt(LocalDateTime.now());

        Shift saved = shiftRepository.save(shift);

        return mapper.toResponse(saved);
    }

    @Override
    public ShiftResponse getActiveByBranch(Long branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found: " + branchId));

        Shift shift = shiftRepository.findByBranchAndStatus(branch, ShiftStatus.OPEN)
                .orElseThrow(() -> new RuntimeException("Active shift is not exist"));

        return mapper.toResponse(shift);
    }

    @Override
    public List<ShiftResponse> getAllByBranch(Long branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found: " + branchId));

        return shiftRepository.findAllByBranch(branch).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<StockMovementResponse> getMovementsByShift(Long id) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift not found: " + id));
        List<StockMovement> stockMovements = stockMovementRepository.findAllByShift(shift);

        return stockMovements.stream()
                .map(stockMovementMapper::toResponse)
                .toList();
    }
}
