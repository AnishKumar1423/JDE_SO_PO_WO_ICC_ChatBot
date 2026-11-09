package com.sales.order.Generate.com.sales.order.Service;

import java.nio.charset.StandardCharsets;
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

import com.sales.order.Generate.com.sales.order.Entity.inventoryCycleCountEntity;

@Service
public class inventoryCycleCountService {

    private final RestTemplate restTemplate = new RestTemplate();

    // Create Cycle Count orchestration
    @Value("${jde.orchestrator.url2:}")
    private String createUrl;

    // Review Cycle Count orchestration
    @Value("${jde.orchestrator.url3:}")
    private String reviewUrl;

    @Value("${jde.username:}")
    private String username;

    @Value("${jde.password:}")
    private String password;

    // CREATE CYCLE COUNT

    public Map<String, Object> createInventoryCycleCount(
            inventoryCycleCountEntity cycleCount) {

        return execute(createUrl, () -> {

            validateCreate(cycleCount);

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("Business Unit", cycleCount.getBusinessUnit());
            body.put("Item Number", cycleCount.getItemNumber());
            body.put("Location", cycleCount.getLocation());
            return body;
        });
    }

    // REVIEW CYCLE COUNT

    public Map<String, Object> reviewInventoryCycleCount(
            inventoryCycleCountEntity cycleCount) {

        return execute(reviewUrl, () -> {

            validateReview(cycleCount);

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("FromCycleStatus", cycleCount.getFromCycleStatus());
            body.put("ThruCycleStatus", cycleCount.getThruCycleStatus());
            body.put("P41240_Version",
                    cycleCount.getP41240Version() == null
                            ? ""
                            : cycleCount.getP41240Version());
            return body;
        });
    }

    // COMMON EXECUTION

    @FunctionalInterface
    private interface BodyBuilder {
        Map<String, Object> build();
    }

    private Map<String, Object> execute(String url, BodyBuilder builder) {

        Map<String, Object> result = new LinkedHashMap<>();

        try {

            // Validates input and builds the request body
            Map<String, Object> requestBody = builder.build();

            if (isBlank(username) || isBlank(password)) {
                throw new IllegalStateException(
                        "Configure JDE_USERNAME and JDE_PASSWORD.");
            }

            if (isBlank(url)) {
                throw new IllegalStateException(
                        "Configure the JDE orchestration URL.");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setBasicAuth(username, password, StandardCharsets.UTF_8);

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map<String, Object>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            request,
                            new ParameterizedTypeReference<Map<String, Object>>() {
                            });

            result.put("success", response.getStatusCode().is2xxSuccessful());
            result.put("httpStatus", response.getStatusCode().value());
            result.put("jdeResponse", response.getBody());

        } catch (HttpStatusCodeException ex) {

            result.put("success", false);
            result.put("httpStatus", ex.getStatusCode().value());
            result.put("error", "JDE returned an HTTP error.");
            result.put("details", ex.getResponseBodyAsString());

        } catch (ResourceAccessException ex) {

            result.put("success", false);
            result.put("httpStatus", 502);
            result.put("error", "Cannot connect to JDE AIS.");
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
            result.put("error", "Failed to execute JDE orchestration request.");
            result.put("details", ex.getMessage());
        }

        return result;
    }

    // VALIDATION

    private void validateCreate(inventoryCycleCountEntity cycleCount) {

        requireNotNull(cycleCount);

        if (isBlank(cycleCount.getBusinessUnit())) {
            throw new IllegalArgumentException("Business Unit is required.");
        }
        if (isBlank(cycleCount.getItemNumber())) {
            throw new IllegalArgumentException("Item Number is required.");
        }
        if (isBlank(cycleCount.getLocation())) {
            throw new IllegalArgumentException("Location is required.");
        }
    }

    private void validateReview(inventoryCycleCountEntity cycleCount) {

        requireNotNull(cycleCount);

        if (isBlank(cycleCount.getFromCycleStatus())) {
            throw new IllegalArgumentException("From Cycle Status is required.");
        }
        if (isBlank(cycleCount.getThruCycleStatus())) {
            throw new IllegalArgumentException("Thru Cycle Status is required.");
        }
    }

    private void requireNotNull(inventoryCycleCountEntity cycleCount) {
        if (cycleCount == null) {
            throw new IllegalArgumentException(
                    "Inventory Cycle Count data cannot be null.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}