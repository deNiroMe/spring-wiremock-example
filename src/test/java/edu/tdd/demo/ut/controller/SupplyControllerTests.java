package edu.tdd.demo.ut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tdd.demo.domain.SaleRecord;
import edu.tdd.demo.domain.SupplyRecord;
import edu.tdd.demo.service.SupplyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({SpringExtension.class})
public class SupplyControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SupplyService supplyService;

    @Test
    @DisplayName("Test - GET /supply/{id} - successful")
    public void testGetSupplyByProductId() throws Exception {

        // Prepare mock supply record
        SupplyRecord supplyRecord = new SupplyRecord(1,"Product","Product Category",10);

        // prepare mocked service method
        doReturn(Optional.of(supplyRecord)).when(supplyService).getSupplyRecord(supplyRecord.getProductId());

        // perform GET Request
        mockMvc.perform(MockMvcRequestBuilders.get("/supply/{id}",supplyRecord.getProductId()))
                // validate 200 OK and JSON response type is received
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))

                //validate response headers
                .andExpect(header().string(HttpHeaders.LOCATION,"/supply/1"))

                // validate response body
                .andExpect(jsonPath("$.productId",is(1)))
                .andExpect(jsonPath("$.productName",is("Product")))
                .andExpect(jsonPath("$.productCategory",is("Product Category")))
                .andExpect(jsonPath("$.quantity",is(10)));
    }

    @Test
    @DisplayName("Test - GET /supply/{id} - failure")
    public void testGetSupplyByProductIdFailure() throws Exception {

         // prepare mocked service method
        doReturn(Optional.empty()).when(supplyService).getSupplyRecord(1);

        // perform GET Request
        mockMvc.perform(MockMvcRequestBuilders.get("/supply/{id}",1))
                // validate 404 NOT_FOUND is received
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test - POST /supply/saleRecord - successful")
    public void testAddSupplyRecordSuccessfully() throws Exception {

        // Prepare mock supply record
        SupplyRecord supplyRecord = new SupplyRecord(1,"Product","Product Category",10);

        // prepare mocked service method
        doReturn(Optional.of(supplyRecord)).when(supplyService).purchaseProduct(1,10);

        // perform GET Request
        mockMvc.perform(
                MockMvcRequestBuilders.post("/supply/saleRecord")
                        .content(new ObjectMapper().writeValueAsString(new SaleRecord(1,10)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))

                // validate 201 CREATED and JSON response type is received
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))

                //validate response headers
                .andExpect(header().string(HttpHeaders.LOCATION,"/supply/1"))

                // validate response body
                .andExpect(jsonPath("$.productId",is(1)))
                .andExpect(jsonPath("$.productName",is("Product")))
                .andExpect(jsonPath("$.productCategory",is("Product Category")))
                .andExpect(jsonPath("$.quantity",is(10)));
    }

}
