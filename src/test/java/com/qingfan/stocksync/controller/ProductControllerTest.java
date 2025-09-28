package com.qingfan.stocksync.controller;

import com.qingfan.stocksync.dto.response.ProductResponse;
import com.qingfan.stocksync.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ProductController controller = new ProductController(productService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void shouldReturnAllProducts() throws Exception {
        // Given
        ProductResponse product = ProductResponse.builder()
                .id(1L)
                .sku("TEST123")
                .name("Test Product")
                .stockQuantity(10)
                .vendor("VendorA")
                .build();

        Page<ProductResponse> productPage = new PageImpl<>(List.of(product), PageRequest.of(0, 10), 1);
        when(productService.getAllProducts(any(PageRequest.class))).thenReturn(productPage);

        // When & Then
        mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].sku").value("TEST123"))
                .andExpect(jsonPath("$.content[0].stockQuantity").value(10))
                .andExpect(jsonPath("$.content[0].vendor").value("VendorA"));
    }

    @Test
    void shouldReturnProductsByVendor() throws Exception {
        // Given
        ProductResponse product = ProductResponse.builder()
                .id(1L)
                .sku("VENDOR123")
                .name("Vendor Product")
                .stockQuantity(5)
                .vendor("VendorA")
                .build();

        Page<ProductResponse> productPage = new PageImpl<>(List.of(product), PageRequest.of(0, 10), 1);
        when(productService.getProductsByVendor(eq("VendorA"), any(PageRequest.class))).thenReturn(productPage);

        // When & Then
        mockMvc.perform(get("/api/products/vendor/VendorA")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].vendor").value("VendorA"))
                .andExpect(jsonPath("$.content[0].sku").value("VENDOR123"))
                .andExpect(jsonPath("$.content[0].stockQuantity").value(5));
    }



}
