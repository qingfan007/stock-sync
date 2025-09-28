package com.qingfan.stocksync.service.impl;

import com.qingfan.stocksync.dto.response.ProductResponse;
import com.qingfan.stocksync.exception.ProductNotFoundException;
import com.qingfan.stocksync.model.entity.Product;
import com.qingfan.stocksync.repository.ProductRepository;
import com.qingfan.stocksync.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(ProductResponse::fromEntity);
    }

    @Override
    public Page<ProductResponse> getProductsByVendor(String vendor, Pageable pageable) {
        Page<Product> productPage = productRepository.findByVendor(vendor, pageable);
        return productPage.map(ProductResponse::fromEntity);
    }

    @Override
    public Page<ProductResponse> getNewlyOutOfStockProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findNewlyOutOfStockProducts(pageable);
        return productPage.map(ProductResponse::fromEntity);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        return ProductResponse.fromEntity(product);
    }
}
