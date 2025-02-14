package com.project.service;

import com.project.models.CashBalanceResponse;
import com.project.models.CashOperationsRequest;
import com.project.service.contracts.OperationsService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class OperationsServiceImpl implements OperationsService {

    private int bgnTotal = 1000;
    private int eurTotal = 2000;
    private Map<Integer, Integer> bgnDenominations = new HashMap<>();
    private Map<Integer, Integer> eurDenominations = new HashMap<>();
    private static final String[] currency = {"BGN", "EUR"};
    private static final String[] operations = {"deposit", "withdrawal"};

    @PostConstruct
    public void init() {
        bgnDenominations.put(50, 10);
        bgnDenominations.put(10, 50);
        eurDenominations.put(100, 10);
        eurDenominations.put(50, 20);
    }

    @Override
    public void processOperation(CashOperationsRequest request) {
        if (operations[0].equalsIgnoreCase(request.getType())) {
            if (request.getCurrency().equals(currency[0])) {
                bgnTotal += request.getAmount();
                updateDenominations(bgnDenominations, request.getDenominations(), true);
            } else if (request.getCurrency().equals(currency[1])) {
                eurTotal += request.getAmount();
                updateDenominations(eurDenominations, request.getDenominations(), true);
            }
        } else if (operations[1].equalsIgnoreCase(request.getType())) {
            if (request.getCurrency().equals(currency[0])) {
                bgnTotal -= request.getAmount();
                updateDenominations(bgnDenominations, request.getDenominations(), false);
            } else if (request.getCurrency().equals(currency[1])) {
                eurTotal -= request.getAmount();
                updateDenominations(eurDenominations, request.getDenominations(), false);
            }
        }
        updateTransactionHistory(request);
        updateBalance();
    }

    private void updateDenominations(Map<Integer, Integer> targetDenominations,
                                     Map<Integer, Integer> requestDenominations,
                                     boolean isDeposit) {
        for (Map.Entry<Integer, Integer> entry : requestDenominations.entrySet()) {
            Integer denomination = entry.getKey();
            Integer count = entry.getValue();
            int initAmount = geTotalAmountOfDenomination(targetDenominations, denomination);
            int updatedAmountDenominations;
            if (isDeposit) {
                updatedAmountDenominations = targetDenominations.getOrDefault(denomination, initAmount) + count;
                targetDenominations.put(denomination, updatedAmountDenominations);
            } else {
                updatedAmountDenominations = targetDenominations.getOrDefault(denomination, initAmount) - count;
                targetDenominations.put(denomination, updatedAmountDenominations);
            }
        }
    }

    private int geTotalAmountOfDenomination(Map<Integer, Integer> targetDenominations, Integer denomination) {
        int defaultValue = 0;
        for (Map.Entry<Integer, Integer> orig : targetDenominations.entrySet()) {
            Integer denominationOrig = orig.getKey();
            if (denominationOrig == denomination) {
                defaultValue = orig.getValue();
            }
        }
        return defaultValue;
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
        try (FileWriter writer = new FileWriter("transaction.txt", true)) {
            if (request.getType().equalsIgnoreCase("deposit")) {
                writer.write(fillTransactionContent(request));
            } else {
                writer.write(fillTransactionContent(request));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String fillTransactionContent(final CashOperationsRequest request) {
        final StringBuilder builder = new StringBuilder();
        builder.append(request.getType().equals("withdrawal") ?
                "\n" + "Withdrawal: " + request.getAmount() + " " + request.getCurrency() + "\n"
                : "\n" + "Deposit: " + request.getAmount() + " " + request.getCurrency() + "\n");
        for (final Map.Entry<Integer, Integer> entry : request.getDenominations().entrySet()) {
            builder.append("Denomination: ")
                    .append(entry.getValue())
                    .append(" ").append("x ")
                    .append(entry.getKey() + " ")
                    .append(request.getCurrency() + "\n");
        }
        return builder.toString();
    }

    private void updateBalance() {
        try (FileWriter writer = new FileWriter("balance.txt")) {
            writer.write(fillBalanceContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String fillBalanceContent() {
        final StringBuilder builder = new StringBuilder();
        appendCurrencyDetails(builder, "BGN", bgnTotal, bgnDenominations);
        appendCurrencyDetails(builder, "EUR", eurTotal, eurDenominations);
        return builder.toString();
    }

    private void appendCurrencyDetails(StringBuilder builder, String currency, int total, Map<Integer, Integer> denominations) {
        builder.append(currency).append(" Total: ").append(total).append("\n")
                .append(currency).append(" Denominations: ").append("\n");
        for (Map.Entry<Integer, Integer> entry : denominations.entrySet()) {
            builder.append(entry.getValue()).append(" x ")
                    .append(entry.getKey()).append(" ").append(currency).append(" ");
        }
        builder.append("\n");
    }
}
