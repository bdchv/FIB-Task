package com.project.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CashBalanceResponse {
    private int bgnTotal;
    private int eurTotal;
    private Map<Integer, Integer> bgnDenominations;
    private Map<Integer, Integer> eurDenominations;
}
