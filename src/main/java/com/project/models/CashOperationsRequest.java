package com.project.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
public class CashOperationsRequest {
    @NotNull
    private String type;
    @NotNull
    private String currency;
    @Min(1)
    private int amount;
    @NotNull
    @NotEmpty
    private Map<Integer, Integer> denominations;
}
