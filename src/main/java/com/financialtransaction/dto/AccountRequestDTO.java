package com.financialtransaction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class AccountRequestDTO {
    @NotBlank
    private String ownerName;
    
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal balance;
    
    @NotBlank
    private String currency;


}
