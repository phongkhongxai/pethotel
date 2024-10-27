package com.bumble.pethotel.controllers;


import com.bumble.pethotel.models.payload.responseModel.PaymentsResponse;
import com.bumble.pethotel.models.payload.responseModel.PetsResponese;
import com.bumble.pethotel.services.PaymentService;
import com.bumble.pethotel.utils.AppConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vn.payos.PayOS;
import vn.payos.type.PaymentLinkData;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PayOS payOS;


    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/create-paymentlink/booking/{id}")
    public ResponseEntity<?> createPayment(@PathVariable("id") Long bookingId, @RequestParam String returnUrl, @RequestParam String cancelUrl) {
        return ResponseEntity.ok(paymentService.createPaymentLink(bookingId,returnUrl,cancelUrl));
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/create-paymentlink/subscription/{userId}")
    public ResponseEntity<?> createPaymentForSubsription(@PathVariable("userId") Long userid,@RequestParam int price, @RequestParam String returnUrl, @RequestParam String cancelUrl) {
        return ResponseEntity.ok(paymentService.createPaymentLinkForSubscription(userid,price, returnUrl,cancelUrl));
    }


    @PostMapping("/confirm-webhook")
    public ObjectNode confirmPayOSWebhook(@RequestBody Map<String, String> requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            String str = payOS.confirmWebhook(requestBody.get("webhookUrl"));
            response.set("data", objectMapper.valueToTree(str));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

    @GetMapping(path = "/{orderId}")
    public ObjectNode getOrderById(@PathVariable("orderId") long orderId) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        try {
            PaymentLinkData order = payOS.getPaymentLinkInformation(orderId);

            response.set("data", objectMapper.valueToTree(order));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }

    }

    @PutMapping("/{orderId}")
    public ObjectNode cancelOrder(@PathVariable("orderId") long orderId) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            PaymentLinkData order = payOS.cancelPaymentLink(orderId, null);
            response.set("data", objectMapper.valueToTree(order));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

    @PostMapping("/payos_webhook")
    public ResponseEntity<ObjectNode> handlePayOSWebhook(@RequestBody ObjectNode webhook) throws JsonProcessingException,IllegalArgumentException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        Webhook webhookBody = objectMapper.treeToValue(webhook, Webhook.class);
        try {
            response.put("error", 0);
            response.put("message", "Webhook received successfully");
            WebhookData data = payOS.verifyPaymentWebhookData(webhookBody);
            System.out.println(data);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatusPayment(@PathVariable("id") Long orderCode,@RequestParam String status) {
        String msg = paymentService.updateStatusPayment(orderCode, status);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
    @GetMapping("/shops/{shopId}")
    public PaymentsResponse getAllSuccessPaymentsOfShop(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                          @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                          @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                          @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
                                          @PathVariable("shopId") Long shopId){
        return paymentService.getAllSuccessPaymentsOfShop(shopId,pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/total")
    public PaymentsResponse getAllPayment(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                          @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                          @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                          @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
                                         ){
        return paymentService.getAllPayments(pageNo, pageSize, sortBy, sortDir);
    }
    @GetMapping("/subscription")
    public PaymentsResponse getAllPaymentSubscription(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                          @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                          @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                          @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return paymentService.getAllPaymentsForSubscription(pageNo, pageSize, sortBy, sortDir);
    }


}
