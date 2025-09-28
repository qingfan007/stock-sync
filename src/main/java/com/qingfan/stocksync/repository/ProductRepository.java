package com.qingfan.stocksync.repository;

import com.qingfan.stocksync.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByVendor(String vendor, Pageable pageable);

    Optional<Product> findBySkuAndVendor(String sku, String vendor);

    // todo
    @Query("SELECT p FROM Product p WHERE p.stockQuantity = 0 AND p.previousStockQuantity > 0")
    Page<Product> findNewlyOutOfStockProducts(Pageable pageable);

}
