package com.project.controller;


import com.project.conf.Config;
import com.project.models.CashBalanceResponse;
import com.project.models.CashOperationsRequest;
import com.project.service.contracts.OperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class OperationsController {

    @Autowired
    private OperationsService operationsService;
    @Autowired
    private Config appConfig;

    @Autowired
    public OperationsController(Config appConfig, OperationsService operationsService) {
        this.appConfig = appConfig;
        this.operationsService = operationsService;
    }

    @PostMapping("/cash-operation")
    public ResponseEntity<String> handleCashOperation(
            @RequestHeader("FIB-X-AUTH") String apiKey, @RequestHeader(value = "username") String username,
            @Valid @RequestBody CashOperationsRequest request) {
        if (!appConfig.getApiKey().equals(apiKey) && !appConfig.getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API Key or UserName");
        }
        operationsService.processOperation(request);
        return ResponseEntity.ok("Operation Successful");
    }

    @GetMapping("/cash-balance")
    public ResponseEntity<CashBalanceResponse> getCashBalance(@RequestHeader("FIB-X-AUTH") String apiKey,
                                                              @RequestHeader(value = "username") String username) {
        if (!appConfig.getApiKey().equals(apiKey) && !appConfig.getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        CashBalanceResponse balanceResponse = operationsService.getBalance();
        return ResponseEntity.ok(balanceResponse);
    }

}
