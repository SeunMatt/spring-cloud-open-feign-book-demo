package com.smattme.democonsumer.integrations;

import com.smattme.democonsumer.responses.ProductResponse;
import com.smattme.democonsumer.services.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InventoryServiceIntegrationTest {

    @Autowired
    private InventoryService inventoryService;

    @Test
    void whenMakePurchase_thenListProductsAndOrderOne() {

        Map<String, Object> response = inventoryService.makePurchase(4);
        assertNotNull(response);

        // assert the product size is 4
        var productsObj = response.get("availableProducts");
        List<ProductResponse> products = (List<ProductResponse>) productsObj;
        assertEquals(4, products.size());

        // assert that we get a successful response status and code from the Order API
        var responseStatusStr = response.get("orderResponseStatus").toString();
        boolean orderResponseStatus = Boolean.parseBoolean(responseStatusStr);
        assertTrue(orderResponseStatus);

        var responseCodeStr = response.get("orderResponseCode").toString();
        int orderResponseCode = Integer.parseInt(responseCodeStr);
        assertEquals(200, orderResponseCode);
    }

    @Test
    void whenMakePurchaseWithZeroQuantityAndCustomExHandling_thenFailedOrder() {

        Map<String, Object> response = inventoryService.makePurchase(0);
        assertNotNull(response);

        // assert the product size is 4
        var productsObj = response.get("availableProducts");
        List<ProductResponse> products = (List<ProductResponse>) productsObj;
        assertEquals(4, products.size());

        // assert that we get a successful response status and code from the Order API
        var orderResponseMessage = response.get("orderResponseMessage").toString();
        assertEquals("Missing required parameter(s)", orderResponseMessage);

        int orderResponseCode = Integer.parseInt(response.get("orderResponseCode").toString());
        assertEquals(400, orderResponseCode);

        List<String> errors = (List<String>) response.get("orderResponseErrors");
        assertEquals(1, errors.size());
        assertEquals("quantity is required", errors.get(0));
    }

}
