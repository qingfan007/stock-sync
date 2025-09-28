package com.qingfan.stocksync.service;

import com.qingfan.stocksync.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<ProductResponse> getAllProducts(Pageable pageable);

    Page<ProductResponse> getProductsByVendor(String vendor, Pageable pageable);

    Page<ProductResponse> getNewlyOutOfStockProducts(Pageable pageable);

    ProductResponse getProductById(Long id);

}
