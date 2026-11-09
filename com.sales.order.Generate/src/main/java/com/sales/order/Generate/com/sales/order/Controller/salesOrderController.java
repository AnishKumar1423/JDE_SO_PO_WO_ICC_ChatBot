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

    public salesOrderController(
            salesOrderService salesOrderService) {

        this.salesOrderService = salesOrderService;
       
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
