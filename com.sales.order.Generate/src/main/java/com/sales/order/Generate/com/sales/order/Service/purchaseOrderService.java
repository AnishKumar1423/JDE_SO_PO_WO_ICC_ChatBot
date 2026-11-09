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

import com.sales.order.Generate.com.sales.order.Entity.purchaseOrderEntity;

@Service
public class purchaseOrderService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${jde.orchestrator.url1}")
    private String orchUrl;

    @Value("${jde.username:}")
    private String username;

    @Value("${jde.password:}")
    private String password;

    public Map<String, Object> createPurchaseOrder(
            purchaseOrderEntity purchaseOrder) {

        Map<String, Object> result = new LinkedHashMap<>();

        try {
            validatePurchaseOrder(purchaseOrder);

            if (username == null || username.isBlank()
                    || password == null || password.isBlank()) {
                throw new IllegalStateException(
                        "Configure JDE_USERNAME and JDE_PASSWORD.");
            }

            if (orchUrl == null || orchUrl.isBlank()) {
                throw new IllegalStateException(
                        "Configure the JDE orchestration URL.");
            }

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            headers.setBasicAuth(
                    username,
                    password,
                    StandardCharsets.UTF_8
            );

            // GridIn_1_3 row (matches Postman)
            Map<String, Object> gridRow = new LinkedHashMap<>();
            gridRow.put("Item_Number", purchaseOrder.getItem_Number1());
            gridRow.put("Quantity_Ordered",
                    purchaseOrder.getQuantity_Ordered1().toPlainString());
            gridRow.put("Unit_Cost",
                    purchaseOrder.getUnit_Cost1().toPlainString());
            gridRow.put("Account_Number",
                    purchaseOrder.getAccount_Number1());

            List<Map<String, Object>> gridRows = new ArrayList<>();
            gridRows.add(gridRow);

            // Request body (matches Postman exactly, no "...1" keys)
            Map<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("Branch_Plant",
                    purchaseOrder.getBranch_Plant1());
            requestBody.put("Long_Address_Number_ALKY",
                    purchaseOrder.getLong_Address_Number_ALKY1());
            requestBody.put("Long_Ship_To_ALKY",
                    purchaseOrder.getLong_Ship_To_ALKY1());
            requestBody.put("Long_Buyer_Address_Number",
                    purchaseOrder.getLong_Buyer_Address_Number1());
            requestBody.put("GridIn_1_3", gridRows);
            requestBody.put("Select_Row",
                    purchaseOrder.getSelect_Row1());
            requestBody.put("P4310_Version",
                    purchaseOrder.getP4310_Version1() == null
                            ? "" : purchaseOrder.getP4310_Version1());

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map<String, Object>> response =
                    restTemplate.exchange(
                            orchUrl,
                            HttpMethod.POST,
                            request,
                            new ParameterizedTypeReference<Map<String, Object>>() {
                            });

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
                    "Cannot connect to JDE AIS.");
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

    private void validatePurchaseOrder(
            purchaseOrderEntity purchaseOrder) {

        if (purchaseOrder == null) {
            throw new IllegalArgumentException(
                    "Purchase order data cannot be null.");
        }

        if (isBlank(purchaseOrder.getBranch_Plant1())) {
            throw new IllegalArgumentException(
                    "Branch_Plant1 is required.");
        }

        if (isBlank(purchaseOrder.getLong_Address_Number_ALKY1())) {
            throw new IllegalArgumentException(
                    "Long_Address_Number_ALKY1 is required.");
        }

        if (isBlank(purchaseOrder.getLong_Ship_To_ALKY1())) {
            throw new IllegalArgumentException(
                    "Long_Ship_To_ALKY1 is required.");
        }

        if (isBlank(
                purchaseOrder.getLong_Buyer_Address_Number1())) {
            throw new IllegalArgumentException(
                    "Long_Buyer_Address_Number1 is required.");
        }

        if (purchaseOrder.getQuantity_Ordered1() == null
                || purchaseOrder.getQuantity_Ordered1()
                .signum() <= 0) {

            throw new IllegalArgumentException(
                    "Quantity_Ordered1 must be greater than zero.");
        }

        if (isBlank(purchaseOrder.getItem_Number1())) {
            throw new IllegalArgumentException(
                    "Item_Number1 is required.");
        }

        if (purchaseOrder.getUnit_Cost1() == null
                || purchaseOrder.getUnit_Cost1().signum() < 0) {
            throw new IllegalArgumentException(
                    "Unit_Cost1 cannot be negative or null.");
        }

        if (isBlank(purchaseOrder.getAccount_Number1())) {
            throw new IllegalArgumentException(
                    "Account_Number1 is required.");
        }

        if (isBlank(purchaseOrder.getSelect_Row1())) {
            throw new IllegalArgumentException(
                    "Select_Row1 is required.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}