package com.project.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CashOperationsRequest {
    @NotNull
    String type;
    @NotNull
    String currency;
    @Min(1)
    int amount;
    @NotNull
    @NotEmpty
    Map<Integer, Integer> denominations;
}
