package com.qingfan.stocksync.service.vendor;

import com.qingfan.stocksync.dto.vendor.ProductStockDto;
import com.qingfan.stocksync.dto.vendor.VendorAProductDto;
import com.qingfan.stocksync.exception.VendorApiException;
import com.qingfan.stocksync.model.enums.VendorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VendorAService implements VendorStockService {

    @Value("${vendor.a.url}")
    private String apiUrl;

    @Value("${vendor.a.timeout}")
    private int timeout;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getVendorName() {
        return VendorType.VENDOR_A.getDisplayName();
    }

    @Override
    @Retryable(
            value = {VendorApiException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public List<ProductStockDto> fetchStockData() {

        log.info("start query {} stock data, URL: {}", getVendorName(), apiUrl);

        try {
            VendorAProductDto[] products = restTemplate.getForObject(apiUrl, VendorAProductDto[].class);

            if (products == null) {
                log.warn("{} return empty data", getVendorName());
                return List.of();
            }

            List<ProductStockDto> result = Arrays.stream(products)
                    .map(this::convertToProductStockDto)
                    .collect(Collectors.toList());

            log.info("success from {} , got product number is {}", getVendorName(), result.size());
            return result;

        } catch (Exception e) {
            log.error("got data from {} fail: {}", getVendorName(), e.getMessage());
            throw new VendorApiException("Call VendorA API fail: " + e.getMessage(), e);
        }

    }

    @Recover
    public List<ProductStockDto> recover(VendorApiException e) {
        log.warn("VendorA API retry three times, but still failed ,return empty, error message: {}", e.getMessage());
        log.info("Downgrade strategy: return an empty product list and continue processing other suppliers");
        return List.of();
    }

    private ProductStockDto convertToProductStockDto(VendorAProductDto vendorProduct) {
        return ProductStockDto.builder()
                .sku(vendorProduct.getSku().trim())
                .name(vendorProduct.getName())
                .stockQuantity(vendorProduct.getStockQuantity())
                .vendor(getVendorName())
                .build();
    }

}
