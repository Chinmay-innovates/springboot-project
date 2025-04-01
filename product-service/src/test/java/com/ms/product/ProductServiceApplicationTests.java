package com.ms.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.product.dto.ProductRequest;
import com.ms.product.repo.ProductRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

    static {
        mongoDBContainer.start();
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepo productRepo;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    @DisplayName("POST /api/product → Create a product")
    void shouldCreateProduct() throws Exception {
        // Arrange
        ProductRequest productRequest = getProductRequest();
        String productRequestString = objectMapper.writeValueAsString(productRequest);

        // Act
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestString)
        ).andExpect(status().isCreated());

        //Assert
        Assertions.assertEquals(1, productRepo.findAll().size());
    }

    @Test
    @DisplayName("GET /api/product → Returns all products")
    void shouldGetAllProducts() throws Exception {
        // Arrange
        ProductRequest productRequest = getProductRequest();
        String productRequestString = objectMapper.writeValueAsString(productRequest);

        // Create a product first
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestString)
        ).andExpect(status().isCreated());

        // Act & Assert
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/product")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    Assertions.assertTrue(responseContent.contains("IPhone 13"));
                });

        // Ensure the product is stored
        Assertions.assertFalse(productRepo.findAll().isEmpty());
    }


    private ProductRequest getProductRequest() {
        return ProductRequest.builder()
                .name("IPhone 13")
                .description("Latest Apple smartphone")
                .price(BigDecimal.valueOf(1200))
                .build();
    }
}
