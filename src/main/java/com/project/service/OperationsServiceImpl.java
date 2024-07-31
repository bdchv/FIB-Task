package com.project.service;

import com.project.models.CashBalanceResponse;
import com.project.models.CashOperationsRequest;
import com.project.service.contracts.OperationsService;
import org.springframework.stereotype.Service;

@Service
public class OperationsServiceImpl implements OperationsService {
    @Override
    public void processOperation(CashOperationsRequest request) {

    }

    @Override
    public CashBalanceResponse getBalance() {
        return null;
    }
}
