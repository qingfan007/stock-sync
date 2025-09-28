package com.qingfan.stocksync.service.impl;

import com.qingfan.stocksync.dto.vendor.ProductStockDto;
import com.qingfan.stocksync.model.entity.Product;
import com.qingfan.stocksync.repository.ProductRepository;
import com.qingfan.stocksync.service.StockSyncService;
import com.qingfan.stocksync.service.vendor.VendorStockService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StockSyncServiceImpl implements StockSyncService {

    private final List<VendorStockService> vendorServices;

    @Autowired
    private ProductRepository productRepository;

    public StockSyncServiceImpl(List<VendorStockService> vendorServices) {
        this.vendorServices = vendorServices;
    }

    // add setter method for test
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Scheduled(fixedRateString = "${stock.sync.interval}")
    @Override
    @Transactional
    public void syncAllVendors() {
        log.info("start full sync vendors stocks data");

        int totalProcessed = 0;
        int totalOutOfStock = 0;

        for (VendorStockService vendorService : vendorServices) {
            try {
                SyncResult result = syncVendor(vendorService);
                totalProcessed += result.processedCount;
                totalOutOfStock += result.outOfStockCount;

                log.info("vendor: {} sync finished: handle the number of product {}, out of stock is {}",
                        vendorService.getVendorName(), result.processedCount, result.outOfStockCount);

            } catch (Exception e) {
                log.error("vendor: {} sync failed: {}", vendorService.getVendorName(), e.getMessage());
                // continue to sync other vendor
            }
        }

        log.info("all vendors sync finished: handle total number: {}, out of stock is {}",
                totalProcessed, totalOutOfStock);
    }

    @Override
    @Transactional
    public void syncVendor(String vendorName) {
        log.info("start to sync vendor [{}] stock data", vendorName);

        VendorStockService vendorService = findVendorService(vendorName);
        if (vendorService == null) {
            throw new IllegalArgumentException("Vendor not found: " + vendorName);
        }

        SyncResult result = syncVendor(vendorService);
        log.info("vendor {} sync finished: handle the number of products: {}, out of stock is {}",
                vendorName, result.processedCount, result.outOfStockCount);
    }

    /**
     * sync single vendor core logic
     */
    private SyncResult syncVendor(VendorStockService vendorService) {
        String vendorName = vendorService.getVendorName();

        // 1. got vendor data
        List<ProductStockDto> stockData = vendorService.fetchStockData();
        if (stockData.isEmpty()) {
            log.warn("vendor: {} return empty data", vendorName);
            return new SyncResult(0, 0);
        }

        int processedCount = 0;
        int outOfStockCount = 0;

        // 2. handle every product
        for (ProductStockDto stockDto : stockData) {
            try {
                boolean isOutOfStock = processProduct(stockDto);
                processedCount++;

                if (isOutOfStock) {
                    outOfStockCount++;
                    log.warn("out of product stock: SKU={}, name={}, vendor={}",
                            stockDto.getSku(), stockDto.getName(), vendorName);
                }

            } catch (Exception e) {
                log.error("handle product failed: SKU={}, error message: {}", stockDto.getSku(), e.getMessage());
            }
        }

        return new SyncResult(processedCount, outOfStockCount);
    }

    /**
     * handle single product stock update
     *
     * @return check if out of stock
     */
    private boolean processProduct(ProductStockDto stockDto) {
        Optional<Product> existingProductOpt = productRepository.findBySkuAndVendor(
                stockDto.getSku(), stockDto.getVendor());

        Product product;
        boolean isOutOfStock = false;

        if (existingProductOpt.isPresent()) {
            // update existing product
            product = existingProductOpt.get();
            int oldStock = product.getStockQuantity();
            int newStock = stockDto.getStockQuantity();

            // check stock change
            if (oldStock > 0 && newStock == 0) {
                isOutOfStock = true;
            }

            // update stock and name
            product.setStockQuantity(newStock);
            if (stockDto.getName() != null) {
                product.setName(stockDto.getName());
            }

        } else {
            // create new product
            product = Product.builder()
                    .sku(stockDto.getSku())
                    .name(stockDto.getName())
                    .stockQuantity(stockDto.getStockQuantity())
                    .vendor(stockDto.getVendor())
                    .build();

            // if new product stock = 0, mark it out of stock
            if (stockDto.getStockQuantity() == 0) {
                isOutOfStock = true;
            }
        }

        product.setLastSyncTime(LocalDateTime.now());
        productRepository.save(product);

        return isOutOfStock;
    }

    /**
     * find service depending on vendor name
     */
    private VendorStockService findVendorService(String vendorName) {
        return vendorServices.stream()
                .filter(service -> service.getVendorName().equals(vendorName))
                .findFirst()
                .orElse(null);
    }

    private static class SyncResult {
        final int processedCount;
        final int outOfStockCount;

        SyncResult(int processedCount, int outOfStockCount) {
            this.processedCount = processedCount;
            this.outOfStockCount = outOfStockCount;
        }
    }

}
