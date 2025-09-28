package com.qingfan.stocksync.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Slf4j
public class CsvFileInitializer implements CommandLineRunner {

    @Value("${vendor.b.file-path}")
    private String csvFilePath;

    @Override
    public void run(String... args) throws Exception {
        createSampleCsvFile();
    }

    private void createSampleCsvFile() {
        try {
            Path filePath = Paths.get(csvFilePath);
            Path directory = filePath.getParent();

            // if directory not exist, create directory
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
                log.info("create directory: {}", directory);
            }

            // if file not exist, create sample file
            if (!Files.exists(filePath)) {
                String csvContent = "sku,name,stockQuantity\n" +
                        "IPHONE13,Apple iPhone 13,15\n" +
                        "IPHONE14,Apple iPhone 14,0\n" +
                        "MACBOOKPRO,MacBook Pro 16-inch,8\n" +
                        "AIRPODS,AirPods Pro,25\n" +
                        "IPAD,iPad Air,0\n" +
                        "IMAC,iMac 24-inch,5\n" +
                        "APPLEWATCH,Apple Watch Series 8,12\n" +
                        "MACMINI,Mac Mini,3\n";

                Files.writeString(filePath, csvContent);
                log.info("sample csv file has beed created: {}", csvFilePath);
                log.info("file include 8 products, 3 out of stock for test");
            } else {
                log.info("csv file has existed: {}", csvFilePath);
            }

        } catch (IOException e) {
            log.warn("create csv file failed, but application continue to run: {}", e.getMessage());
        }
    }

}
