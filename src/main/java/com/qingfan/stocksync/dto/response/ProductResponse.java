package com.qingfan.stocksync.dto.response;

import com.qingfan.stocksync.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private String sku;
    private String name;
    private Integer stockQuantity;
    private String vendor;
    private LocalDateTime lastSyncTime;
    private LocalDateTime updatedAt;

    public static ProductResponse fromEntity(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .stockQuantity(product.getStockQuantity())
                .vendor(product.getVendor())
                .lastSyncTime(product.getLastSyncTime())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

}
