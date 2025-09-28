package com.qingfan.stocksync.service;

public interface StockSyncService {

    /**
     * sync all vendors stocks data
     */
    void syncAllVendors();

    /**
     * sync vendor stock data
     */
    void syncVendor(String vendorName);

}
