package com.project.controller;


import com.project.models.CashBalanceResponse;
import com.project.models.CashOperationsRequest;
import com.project.service.contracts.OperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class OperationsController {
    @Autowired
    private OperationsService operationsService;

    @PostMapping("/cash-operation")
    public ResponseEntity<String> handleCashOperation(
            @RequestHeader("FIB-X-AUTH") String apiKey,
            @Valid @RequestBody CashOperationsRequest request) {
        if (!"f9Uie8nNf112hx8s".equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API Key");
        }
        operationsService.processOperation(request);
        return ResponseEntity.ok("Operation Successful");
    }

    @GetMapping("/cash-balance")
    public ResponseEntity<CashBalanceResponse> getCashBalance(@RequestHeader("FIB-X-AUTH") String apiKey) {
        if (!"f9Uie8nNf112hx8s".equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        CashBalanceResponse balanceResponse = operationsService.getBalance();
        return ResponseEntity.ok(balanceResponse);
    }

}
