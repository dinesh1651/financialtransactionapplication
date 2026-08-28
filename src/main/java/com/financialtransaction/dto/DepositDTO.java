package com.financialtransaction.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositDTO {

    @NotNull(message = "Balance is required")
    @Positive(message = "Deposit balance must be greater than zero")
    private BigDecimal balance;

}
