package com.qingfan.stocksync.controller;

import com.qingfan.stocksync.dto.vendor.VendorAProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/mock/vendor-a")
@Slf4j
public class MockVendorAController {

    private final Random random = new Random();

    private int failCount = 0;

    /**
     * http://localhost:8080/mock/vendor-a/stock
     */
    @GetMapping("/stock")
    public List<VendorAProductDto> getStockData() {
        log.info("Call Vendor A API, return mock stock data");

        return Arrays.asList(
                new VendorAProductDto("SKU001", "smartphone", generateStock(5, 20)),
                new VendorAProductDto("SKU002", "computer", generateStock(0, 15)),
                new VendorAProductDto("SKU003", "wireless headphones", generateStock(10, 30)),
                new VendorAProductDto("SKU004", "Smartwatch", generateStock(0, 25)),
                new VendorAProductDto("SKU005", "ipad", generateStock(8, 12)),
                new VendorAProductDto("SKU006", "camera", generateStock(3, 8)),
                new VendorAProductDto("SKU007", "game consoles", generateStock(15, 20)),
                new VendorAProductDto("SKU008", "bluetooth speakers", generateStock(0, 10))
        );
    }

    @GetMapping("/stock/unreliable")
    public List<VendorAProductDto> getUnreliableStockData() {
        failCount++;
        log.info("unreliable API call，count: {}", failCount);


        if (failCount <= 2) {
            throw new RuntimeException("Vendor A API can not be used temp - mock fail");
        }

        return Arrays.asList(
                new VendorAProductDto("RETRY001", "retry test product", 5),
                new VendorAProductDto("RETRY002", "another retry test product", 8)
        );

    }

    private int generateStock(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }


}
