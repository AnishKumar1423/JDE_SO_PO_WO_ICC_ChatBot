package com.sales.order.Generate.com.sales.order.Controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sales.order.Generate.com.sales.order.Entity.inventoryCycleCountEntity;
import com.sales.order.Generate.com.sales.order.Entity.purchaseOrderEntity;
import com.sales.order.Generate.com.sales.order.Entity.salesOrderEntity;
import com.sales.order.Generate.com.sales.order.Service.inventoryCycleCountService;
import com.sales.order.Generate.com.sales.order.Service.purchaseOrderService;
import com.sales.order.Generate.com.sales.order.Service.salesOrderService;

@RestController
@RequestMapping
public class salesOrderController {

    private final salesOrderService salesOrderService;
    private final purchaseOrderService purchaseOrderService;
    private final inventoryCycleCountService inventoryCycleCountService;

    public salesOrderController(
            salesOrderService salesOrderService,
            purchaseOrderService purchaseOrderService,
            inventoryCycleCountService inventoryCycleCountService) {

        this.salesOrderService = salesOrderService;
        this.purchaseOrderService = purchaseOrderService;
        this.inventoryCycleCountService = inventoryCycleCountService;
    }

    // SALES ORDER

    @PostMapping("/sales-order/create")
    public ResponseEntity<?> createSalesOrder(
            @RequestBody salesOrderEntity request) {

        try {
            return buildResponse(salesOrderService.createSalesOrder(request));
        } catch (Exception e) {
            return errorResponse("Failed to create JDE Sales Order", e);
        }
    }

    // PURCHASE ORDER
    @PostMapping("/purchase-order/create")
    public ResponseEntity<?> createPurchaseOrder(
            @RequestBody purchaseOrderEntity request) {

        try {
            return buildResponse(purchaseOrderService.createPurchaseOrder(request));
        } catch (Exception e) {
            return errorResponse("Failed to create JDE Purchase Order", e);
        }
    }

    // INVENTORY CYCLE COUNT
    // Create Cycle Count
    @PostMapping("/inventoryCycleCount/create")
    public ResponseEntity<?> createInventoryCycleCount(
            @RequestBody inventoryCycleCountEntity request) {

        try {
            return buildResponse(
                    inventoryCycleCountService.createInventoryCycleCount(request));
        } catch (Exception e) {
            return errorResponse("Failed to create JDE Inventory Cycle Count", e);
        }
    }

    // Review Cycle Count (POST: a GET request body is dropped by many clients/proxies)
    @PostMapping("/inventoryCycleCount/reviews")
    public ResponseEntity<?> reviewInventoryCycleCount(
            @RequestBody inventoryCycleCountEntity request) {

        try {
            return buildResponse(
                    inventoryCycleCountService.reviewInventoryCycleCount(request));
        } catch (Exception e) {
            return errorResponse("Failed to review JDE Inventory Cycle Count", e);
        }
    }
    
    // COMMON HANDLERS
    private ResponseEntity<?> errorResponse(String message, Exception e) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "success", false,
                        "message", message,
                        "error", String.valueOf(e.getMessage())));
    }

    private ResponseEntity<?> buildResponse(Map<String, Object> response) {

        Object statusValue = response.get("httpStatus");

        if (statusValue instanceof Number) {

            int status = ((Number) statusValue).intValue();

            if (status >= 400 && status <= 599) {
                return ResponseEntity.status(status).body(response);
            }
        }

        if (Boolean.TRUE.equals(response.get("success"))) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(response);
    }
}