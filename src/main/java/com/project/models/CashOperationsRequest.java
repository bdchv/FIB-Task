package com.project.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CashOperationsRequest {
    private String type;
    private String currency;
    private int amount;
    private Map<Integer, Integer> denominations;
}
