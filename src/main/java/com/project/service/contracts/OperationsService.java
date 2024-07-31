package com.project.service.contracts;

import com.project.models.CashBalanceResponse;
import com.project.models.CashOperationsRequest;


public interface OperationsService {

    void processOperation(CashOperationsRequest request);
    CashBalanceResponse getBalance();
}
