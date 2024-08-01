package com.project.service;

import com.project.models.CashBalanceResponse;
import com.project.models.CashOperationsRequest;
import com.project.service.contracts.OperationsService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class OperationsServiceImpl implements OperationsService {

    private int bgnTotal = 1000;
    private int eurTotal = 2000;
    private Map<Integer, Integer> bgnDenominations = new HashMap<>();
    private Map<Integer, Integer> eurDenominations = new HashMap<>();

    @PostConstruct
    public void init() {
        bgnDenominations.put(50, 10);
        bgnDenominations.put(10, 50);
        eurDenominations.put(100, 10);
        eurDenominations.put(50, 20);
    }

    @Override
    public void processOperation(CashOperationsRequest request) {
        if ("deposit".equalsIgnoreCase(request.getType())) {
            if (request.getCurrency().equals("BGN")) {
                bgnTotal += request.getAmount();
                for (Map.Entry<Integer, Integer> entry : request.getDenominations().entrySet()) {
                    Integer denomination = entry.getKey();
                    Integer count = entry.getValue();
                    if (bgnDenominations.containsKey(denomination)) {
                        if (denomination == 10) {
                            bgnDenominations.put(denomination, bgnDenominations.getOrDefault(denomination, 50) + count);
                        } else {
                            bgnDenominations.put(denomination, bgnDenominations.getOrDefault(denomination, 10) + count);
                        }
                    }
                }
            } else {
                eurTotal += request.getAmount();
                for (Map.Entry<Integer, Integer> entry : request.getDenominations().entrySet()) {
                    Integer denomination = entry.getKey();
                    Integer count = entry.getValue();
                    if (denomination == 50) {
                        eurDenominations.put(denomination, eurDenominations.getOrDefault(denomination, 20) + count);
                    } else {
                        eurDenominations.put(20, 5);
                    }
                }
            }
        } else if ("withdrawal".equalsIgnoreCase(request.getType())) {
            if (request.getCurrency().equals("BGN")) {
                bgnTotal -= request.getAmount();
                for (Map.Entry<Integer, Integer> entry : request.getDenominations().entrySet()) {
                    Integer denomination = entry.getKey();
                    Integer count = entry.getValue();
                    if (denomination == 10) {
                        bgnDenominations.put(denomination, bgnDenominations.getOrDefault(denomination, 50) - count);
                    } else {
                        bgnDenominations.put(denomination, bgnDenominations.getOrDefault(denomination, 10) - count);
                    }
                }
            } else {
                eurTotal -= request.getAmount();
                for (Map.Entry<Integer, Integer> entry : request.getDenominations().entrySet()) {
                    Integer denomination = entry.getKey();
                    Integer count = entry.getValue();
                    if (denomination == 50) {
                        eurDenominations.put(denomination, eurDenominations.getOrDefault(denomination, 10) - count);
                    }
                }
            }
        }
        updateTransactionHistory(request);
        updateBalance();
    }

    @Override
    public CashBalanceResponse getBalance() {
        CashBalanceResponse response = new CashBalanceResponse();
        response.setBgnTotal(bgnTotal);
        response.setEurTotal(eurTotal);
        response.setBgnDenominations(new HashMap<>(bgnDenominations));
        response.setEurDenominations(new HashMap<>(eurDenominations));
        return response;
    }


    private void updateTransactionHistory(CashOperationsRequest request) {
        try {
            FileWriter writer = null;
            if (checkDay()) {
                writer = new FileWriter("transaction.txt", true);
            } else {
                writer = new FileWriter("transaction.txt");
            }
            if (request.getType().equalsIgnoreCase("deposit")) {
                writer.write("Deposit: " + request.getAmount() + " " + request.getCurrency() + "\n");
            } else {
                writer.write("Withdrawal: " + request.getAmount() + " " + request.getCurrency() + "\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkDay() {
        boolean isSameDay = true;
        LocalDate date = LocalDate.now();
        LocalDate previousDate = LocalDate.now().minusDays(1);
        if (date.equals(previousDate)) {
            isSameDay = false;
        }
        return isSameDay;
    }

    private void updateBalance() {
        try (FileWriter writer = new FileWriter("balance.txt")) {
            writer.write("BGN Total:" + bgnTotal + "\n");
            writer.write("BGN Denominations: " + bgnDenominations + "\n");
            writer.write("EUR Total: " + eurTotal + "\n");
            writer.write("EUR Denominations: " + eurDenominations + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
