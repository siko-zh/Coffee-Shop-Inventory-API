package com.zholdigaliev.coffeeshopims.repository;

import com.zholdigaliev.coffeeshopims.dto.StockDto.StockResponse;
import com.zholdigaliev.coffeeshopims.entity.Branch;
import com.zholdigaliev.coffeeshopims.entity.Product;
import com.zholdigaliev.coffeeshopims.entity.Stock;
import com.zholdigaliev.coffeeshopims.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findStockByBranchAndProduct(Branch branch, Product product);

    List<Stock> findAllByBranchId(Long branchId);

    @Query("SELECT s FROM Stock s WHERE s.branch.id = :branchId AND s.quantity <= s.minQuantity")
    List<Stock> findLowStockByBranchId(@Param("branchId") Long branchId);
}
