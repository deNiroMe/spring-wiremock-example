package edu.tdd.demo.controller;

import edu.tdd.demo.domain.SaleRecord;
import edu.tdd.demo.service.SupplyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RestController
@AllArgsConstructor
public class SupplyController {

    private SupplyService supplyService;

    @GetMapping("/supply/{id}")
    private ResponseEntity<?> getSupplyRecord(@PathVariable Integer id) {

        log.debug("Finding supply record for product with id : {}", id);

        return supplyService.getSupplyRecord(id)
                .map( supplyRecord -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .location(new URI("/supply/"+id))
                                .body(supplyRecord);
                    } catch (URISyntaxException e){
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/supply/saleRecord")
    private ResponseEntity<?> newSaleRecord(@RequestBody SaleRecord saleRecord) {

        log.debug("creating a new sale record");

        return supplyService.purchaseProduct(saleRecord.getProductId(),saleRecord.getQuantity())
                .map( supplyRecord -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .location(new URI("/supply/"+supplyRecord.getProductId()))
                                .body(supplyRecord);
                    } catch (URISyntaxException e){
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
