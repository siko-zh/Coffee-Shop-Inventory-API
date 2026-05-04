package com.zholdigaliev.coffeeshopims.repository;

import com.zholdigaliev.coffeeshopims.dto.ProductDto.ProductResponse;
import com.zholdigaliev.coffeeshopims.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByCategoryId(Long categoryId);
}
