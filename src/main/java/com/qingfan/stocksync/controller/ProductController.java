package com.qingfan.stocksync.controller;

import com.qingfan.stocksync.dto.response.ProductResponse;
import com.qingfan.stocksync.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        log.info("query product info: ID={}", id);
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        log.info("Pagination query product info - page: {}, size: {}, sort: {}", page, size, sortBy);

        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProductResponse> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);

    }

    @GetMapping("/vendor/{vendor}")
    public ResponseEntity<Page<ProductResponse>> getProductsByVendor(
            @PathVariable String vendor,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("pagination query vendor [{}] product - page: {}, size: {}", vendor, page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("lastSyncTime").descending());
        Page<ProductResponse> products = productService.getProductsByVendor(vendor, pageable);
        return ResponseEntity.ok(products);

    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<Page<ProductResponse>> getNewlyOutOfStockProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("pagination query newly out of stock products - page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        Page<ProductResponse> products = productService.getNewlyOutOfStockProducts(pageable);
        return ResponseEntity.ok(products);

    }


}
