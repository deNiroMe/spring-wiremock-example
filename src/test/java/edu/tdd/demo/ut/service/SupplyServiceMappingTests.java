package edu.tdd.demo.ut.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.tdd.demo.domain.SupplyRecord;
import edu.tdd.demo.service.SupplyService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

@SpringBootTest
@TestPropertySource(value = "classpath:test.properties")
public class SupplyServiceMappingTests {

    @Autowired
    private SupplyService supplyService;

    private WireMockServer wireMockServer;

    @BeforeEach
    public void setUpWireMockServer() {
        // initialize wire mock server
        wireMockServer = new WireMockServer(9090);
        wireMockServer.start();
    }

    @AfterEach
    public void stopWireMockServer() { wireMockServer.stop(); }

    @Test
    @DisplayName("Test - get supply record successfully")
    public void testGetSupplyRecordSuccessfully() {

        Optional<SupplyRecord> supplyRecord = supplyService.getSupplyRecord(1);

        Assertions.assertTrue(supplyRecord.isPresent(),"Supply record should exist");
    }

    @Test
    @DisplayName("Test - get supply record failure")
    public void testGetSupplyRecordFailure() {

        Optional<SupplyRecord> supplyRecord = supplyService.getSupplyRecord(2);

        Assertions.assertFalse(supplyRecord.isPresent(),"Supply record should not exist");
    }

    @Test
    @DisplayName("Test - purchase product successfully")
    public void testPurchaseProductSuccessfully() {

        Optional<SupplyRecord> supplyRecord = supplyService.purchaseProduct(1,100);

        Assertions.assertTrue(supplyRecord.isPresent(),"Supply record should exist");
        Assertions.assertEquals(1200,supplyRecord.get().getQuantity().intValue(),"New Supply quantity should be 1200");
    }
}
