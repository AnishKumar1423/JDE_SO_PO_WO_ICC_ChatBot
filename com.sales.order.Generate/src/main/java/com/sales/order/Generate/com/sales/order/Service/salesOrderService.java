
package com.sales.order.Generate.com.sales.order.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.sales.order.Generate.com.sales.order.Entity.salesOrderEntity;

@Service
public class salesOrderService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${jde.orchestrator.url}")
    private String orchUrl;

    @Value("${jde.username:}")
    private String username;

    @Value("${jde.password:}")
    private String password;

    public Map<String, Object> createSalesOrder(
            salesOrderEntity salesOrder) {

        Map<String, Object> result = new LinkedHashMap<>();

        try {
            // 1. Validate the request
            validateSalesOrder(salesOrder);

            if (username == null || username.isBlank()
                    || password == null || password.isBlank()) {
                throw new IllegalStateException(
                        "Configure JDE_USERNAME and JDE_PASSWORD.");
            }

            if (orchUrl == null || orchUrl.isBlank()) {
                throw new IllegalStateException(
                        "Configure the JDE orchestration URL.");
            }

            // 2. Configure HTTP headers
            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(
                    List.of(MediaType.APPLICATION_JSON));

            headers.setBasicAuth(
                    username,
                    password,
                    StandardCharsets.UTF_8);

            // 3. Prepare one sales order detail grid row
            Map<String, Object> gridRow = new LinkedHashMap<>();

            gridRow.put(
                    "Quantity_Ordered",
                    salesOrder.getQuantity_Ordered());

            gridRow.put(
                    "Item_Number",
                    salesOrder.getItem_Number());

            gridRow.put(
                    "Unit_Price",
                    salesOrder.getUnit_Price());

            List<Map<String, Object>> gridRows = new ArrayList<>();
            gridRows.add(gridRow);

            // 4. Prepare orchestration input
            Map<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put(
                    "Business_Unit",
                    salesOrder.getBusiness_Unit());

            requestBody.put(
                    "Long_Address_Number",
                    salesOrder.getLong_Address_Number());

            // Must match the configured JDE orchestration input name
            requestBody.put(
                    "Long_Address_Number-Shipto",
                    salesOrder.getLong_Address_Number_Shipto());

            requestBody.put("GridIn_1_3", gridRows);

            requestBody.put(
                    "Select_Row",
                    salesOrder.getSelect_Row());

            // 5. Execute the POST request
            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map<String, Object>> response =
                    restTemplate.exchange(
                            orchUrl,
                            HttpMethod.POST,
                            request,
                            new ParameterizedTypeReference<
                                    Map<String, Object>>() {
                            });

            // 6. Process the JDE response
            result.put(
                    "success",
                    response.getStatusCode().is2xxSuccessful());

            result.put(
                    "httpStatus",
                    response.getStatusCode().value());

            result.put("jdeResponse", response.getBody());

        } catch (HttpStatusCodeException ex) {

            result.put("success", false);
            result.put("httpStatus", ex.getStatusCode().value());
            result.put("error", "JDE returned an HTTP error.");
            result.put(
                    "details",
                    ex.getResponseBodyAsString());

        } catch (ResourceAccessException ex) {

            result.put("success", false);
            result.put("httpStatus", 502);
            result.put(
                    "error",
                    "Cannot connect to JDE AIS. Check the URL, "
                            + "network, VPN, and AIS availability.");
            result.put("details", ex.getMessage());

        } catch (IllegalArgumentException ex) {

            result.put("success", false);
            result.put("httpStatus", 400);
            result.put("error", ex.getMessage());

        } catch (IllegalStateException ex) {

            result.put("success", false);
            result.put("httpStatus", 500);
            result.put("error", ex.getMessage());

        } catch (RestClientException ex) {

            result.put("success", false);
            result.put("httpStatus", 502);
            result.put(
                    "error",
                    "Failed to execute the JDE orchestration request.");
            result.put("details", ex.getMessage());
        }

        return result;
    }

    private void validateSalesOrder(salesOrderEntity salesOrder) {

        if (salesOrder == null) {
            throw new IllegalArgumentException(
                    "Sales order data cannot be null.");
        }

        if (isBlank(salesOrder.getBusiness_Unit())) {
            throw new IllegalArgumentException(
                    "Business_Unit is required.");
        }

        if (isBlank(salesOrder.getLong_Address_Number())) {
            throw new IllegalArgumentException(
                    "Long_Address_Number is required.");
        }

        if (isBlank(salesOrder.getLong_Address_Number_Shipto())) {
            throw new IllegalArgumentException(
                    "Long_Address_Number_Shipto is required.");
        }

        if (salesOrder.getQuantity_Ordered() == null
                || salesOrder.getQuantity_Ordered().signum() <= 0) {
            throw new IllegalArgumentException(
                    "Quantity_Ordered must be greater than zero.");
        }

        if (isBlank(salesOrder.getItem_Number())) {
            throw new IllegalArgumentException(
                    "Item_Number is required.");
        }

        if (salesOrder.getUnit_Price() == null
                || salesOrder.getUnit_Price().signum() < 0) {
            throw new IllegalArgumentException(
                    "Unit_Price cannot be negative or null.");
        }

        if (isBlank(salesOrder.getSelect_Row())) {
            throw new IllegalArgumentException(
                    "Select_Row is required.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
    
    
}
