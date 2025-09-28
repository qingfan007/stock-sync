package com.qingfan.stocksync.service.vendor;

import com.qingfan.stocksync.dto.vendor.ProductStockDto;
import com.qingfan.stocksync.exception.VendorFileException;
import com.qingfan.stocksync.model.enums.VendorType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class VendorBService implements VendorStockService {

    @Value("${vendor.b.file-path}")
    private String filePath;

    @Override
    public String getVendorName() {
        return VendorType.VENDOR_B.getDisplayName();
    }

    @Override
    public List<ProductStockDto> fetchStockData() {
        log.info("start read {} CSV file: {}", getVendorName(), filePath);

        Path csvPath = Paths.get(filePath);

        if (!Files.exists(csvPath)) {
            log.warn("CSV file not found: {}", filePath);
            return List.of();
        }

        try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8);
             CSVParser csvParser = CSVFormat.DEFAULT.builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setIgnoreHeaderCase(true)
                     .setTrim(true)
                     .build()
                     .parse(reader)) {

            List<ProductStockDto> products = new ArrayList<>();

            for (CSVRecord record : csvParser) {
                try {
                    ProductStockDto product = parseCsvRecord(record);
                    if (product != null) {
                        products.add(product);
                    }
                } catch (Exception e) {
                    log.warn("parse CSV record fail，skip: {}", record, e);
                }
            }

            log.info("success read from {} ,got product number : {}", getVendorName(), products.size());
            return products;

        } catch (Exception e) {
            log.error("read {} CSV file fail: {}", getVendorName(), e.getMessage());
            throw new VendorFileException("handle VendorB file fail: " + e.getMessage(), e);
        }

    }

    private ProductStockDto parseCsvRecord(CSVRecord record) {
        try {
            String sku = record.get("sku");
            String name = record.get("name");
            String stockStr = record.get("stockQuantity");

            if (sku == null || sku.trim().isEmpty()) {
                log.warn("SKU is empty，skip record: {}", record);
                return null;
            }

            int stockQuantity;
            try {
                stockQuantity = Integer.parseInt(stockStr);
                //  make sure stock >= 0
                stockQuantity = Math.max(0, stockQuantity);
            } catch (NumberFormatException e) {
                log.warn("stock number format error: {}, use default 0", stockStr);
                stockQuantity = 0;
            }

            return ProductStockDto.builder()
                    .sku(sku.trim())
                    .name(name != null ? name.trim() : "Unknown Product")
                    .stockQuantity(stockQuantity)
                    .vendor(getVendorName())
                    .build();

        } catch (Exception e) {
            log.warn("parse CSV record error: {}, error message: {}", record, e.getMessage());
            return null;
        }
    }

}
