package com.qingfan.stocksync.controller;

import com.qingfan.stocksync.service.StockSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sync")
@Slf4j
public class SyncController {

    @Autowired
    private StockSyncService stockSyncService;

    @PostMapping("/all")
    public ResponseEntity<String> syncAllVendors() {
        log.info("manually trigger full sync vendors");
        stockSyncService.syncAllVendors();
        return ResponseEntity.ok("full sync task has been triggered!!!");
    }

    @PostMapping("/vendor/{vendorName}")
    public ResponseEntity<String> syncVendor(@PathVariable String vendorName) {
        log.info("manually trigger vendor sync: {}", vendorName);
        stockSyncService.syncVendor(vendorName);
        return ResponseEntity.ok("vendor:  " + vendorName + " sync task has been triggered!!!");
    }

}
