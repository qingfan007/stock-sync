package com.qingfan.stocksync;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(properties = {
        "stock.sync.enabled=false",  // 测试环境禁用定时任务
        "vendor.a.url=http://localhost:8080/mock/vendor-a/stock",
        "vendor.b.file-path=src/test/resources/vendor-b/stock.csv"
})
class StockSyncApplicationTests {

    @Test
    void contextLoads() {
        assertTrue(true, "application start success");
    }

}
