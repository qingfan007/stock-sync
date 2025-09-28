package com.qingfan.stocksync.dto.vendor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockDto {

    private String sku;
    private String name;
    private Integer stockQuantity;
    private String vendor;

}
