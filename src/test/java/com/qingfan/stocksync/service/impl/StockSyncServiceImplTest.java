package com.qingfan.stocksync.service.impl;

import com.qingfan.stocksync.dto.vendor.ProductStockDto;
import com.qingfan.stocksync.model.entity.Product;
import com.qingfan.stocksync.repository.ProductRepository;
import com.qingfan.stocksync.service.vendor.VendorAService;
import com.qingfan.stocksync.service.vendor.VendorBService;
import com.qingfan.stocksync.service.vendor.VendorStockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockSyncServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private VendorAService vendorAService;

    @Mock
    private VendorBService vendorBService;

    private StockSyncServiceImpl stockSyncService;

    @BeforeEach
    void setUp() {

        lenient().when(vendorAService.getVendorName()).thenReturn("VendorA");
        lenient().when(vendorBService.getVendorName()).thenReturn("VendorB");

        List<VendorStockService> vendorServices = Arrays.asList(vendorAService, vendorBService);
        stockSyncService = new StockSyncServiceImpl(vendorServices);
        stockSyncService.setProductRepository(productRepository);

    }


    @Test
    void shouldDetectOutOfStockWhenStockChangesFromPositiveToZero() {
        // Given - old stock is 5, new stock is 0 (should be detected out of stock)）
        Product existingProduct = Product.builder()
                .id(1L)
                .sku("IPHONE14")
                .name("iPhone 14")
                .stockQuantity(5)  // old stock >0
                .vendor("VendorA")
                .build();

        ProductStockDto newStock = ProductStockDto.builder()
                .sku("IPHONE14")
                .name("iPhone 14")
                .stockQuantity(0)   // new stock =0
                .vendor("VendorA")
                .build();

        when(productRepository.findBySkuAndVendor("IPHONE14", "VendorA"))
                .thenReturn(Optional.of(existingProduct));
        when(vendorAService.fetchStockData()).thenReturn(List.of(newStock));

        System.out.println("Testing: shouldDetectOutOfStockWhenStockChangesFromPositiveToZero");

        // When
        stockSyncService.syncVendor("VendorA");

        // Then
        verify(productRepository, times(1)).save(existingProduct);
        verify(vendorAService, times(1)).fetchStockData();
    }

    @Test
    void shouldCreateNewProductWhenNotExists() {
        // Given - if new product not exist in DB
        ProductStockDto newStock = ProductStockDto.builder()
                .sku("NEW123")
                .name("New Product")
                .stockQuantity(10)
                .vendor("VendorA")
                .build();

        when(productRepository.findBySkuAndVendor("NEW123", "VendorA"))
                .thenReturn(Optional.empty()); // product not exist
        when(vendorAService.fetchStockData()).thenReturn(List.of(newStock));

        System.out.println("Testing: shouldCreateNewProductWhenNotExists");

        // When
        stockSyncService.syncVendor("VendorA");

        // Then
        verify(productRepository, times(1)).save(any(Product.class));
        verify(vendorAService, times(1)).fetchStockData();
    }

    @Test
    void shouldUpdateExistingProductStock() {
        // Given - existing product update stock
        Product existingProduct = Product.builder()
                .id(1L)
                .sku("MACBOOK")
                .name("MacBook Pro")
                .stockQuantity(8)
                .vendor("VendorA")
                .build();

        ProductStockDto newStock = ProductStockDto.builder()
                .sku("MACBOOK")
                .name("MacBook Pro")
                .stockQuantity(15)  // stock update
                .vendor("VendorA")
                .build();

        when(productRepository.findBySkuAndVendor("MACBOOK", "VendorA"))
                .thenReturn(Optional.of(existingProduct));
        when(vendorAService.fetchStockData()).thenReturn(List.of(newStock));

        System.out.println("Testing: shouldUpdateExistingProductStock");

        // When
        stockSyncService.syncVendor("VendorA");

        // Then - verify product update
        verify(productRepository, times(1)).save(existingProduct);
        verify(vendorAService, times(1)).fetchStockData();
    }


}
