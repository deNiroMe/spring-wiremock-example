package edu.tdd.demo.service;

import edu.tdd.demo.domain.SaleRecord;
import edu.tdd.demo.domain.SupplyRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class SupplyService {

    @Value("${supply_provider.uri}")
    private String supplyServiceURL;

    // will help us make HTTP requests to other services
    private RestTemplate restTemplate = new RestTemplate();

    public Optional<SupplyRecord> getSupplyRecord(Integer id) {
        try {
            return Optional.ofNullable(restTemplate.getForObject(supplyServiceURL+"/supply/"+id,SupplyRecord.class));
        } catch (HttpClientErrorException ex) {
            return Optional.empty();
        }
    }

    public Optional<SupplyRecord> purchaseProduct(Integer productId,Integer quantity) {
        try {
            return Optional.ofNullable(restTemplate.postForObject(supplyServiceURL+"/supply/"+productId+"/saleRecord",
                    new SaleRecord(productId,quantity),
                    SupplyRecord.class));
        } catch (HttpClientErrorException ex) {
            return Optional.empty();
        }
    }
}
