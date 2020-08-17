package edu.tdd.demo.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import edu.tdd.demo.domain.SaleRecord;
import edu.tdd.demo.domain.SupplyRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({SpringExtension.class})
@TestPropertySource(value = "classpath:test.properties")
public class SupplyServiceIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

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
    @DisplayName("Test - GET /supply/{id} - successful")
    public void testGetSupplyByProductId() throws Exception {

        // perform GET Request
        mockMvc.perform(MockMvcRequestBuilders.get("/supply/{id}",1))
                // validate 200 OK and JSON response type is received
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))

                //validate response headers
                .andExpect(header().string(HttpHeaders.LOCATION,"/supply/1"))

                // validate response body
                .andExpect(jsonPath("$.productId",is(1)))
                .andExpect(jsonPath("$.productName",is("New Product")))
                .andExpect(jsonPath("$.productCategory",is("Product Category")))
                .andExpect(jsonPath("$.quantity",is(1300)));
    }

    @Test
    @DisplayName("Test - GET /supply/{id} - failure")
    public void testGetSupplyByProductIdFailure() throws Exception {

        // perform GET Request
        mockMvc.perform(MockMvcRequestBuilders.get("/supply/{id}",10))
                // validate 404 NOT_FOUND is received
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test - POST /supply/saleRecord - successful")
    public void testAddSupplyRecordSuccessfully() throws Exception {

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
                .andExpect(jsonPath("$.productName",is("New Product")))
                .andExpect(jsonPath("$.productCategory",is("Product Category")))
                .andExpect(jsonPath("$.quantity",is(1200)));
    }

}
