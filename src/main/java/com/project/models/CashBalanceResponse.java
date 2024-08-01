package com.project.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CashBalanceResponse {
    int bgnTotal;
    int eurTotal;
    Map<Integer, Integer> bgnDenominations;
    Map<Integer, Integer> eurDenominations;
}
