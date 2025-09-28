package com.qingfan.stocksync.service.vendor;

import com.qingfan.stocksync.dto.vendor.ProductStockDto;

import java.util.List;

public interface VendorStockService {

    String getVendorName();

    List<ProductStockDto> fetchStockData();

}
