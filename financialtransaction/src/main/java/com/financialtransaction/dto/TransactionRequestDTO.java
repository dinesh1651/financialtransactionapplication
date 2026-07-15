package com.financialtransaction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

    @Data
    public class TransactionRequestDTO {

        @NotNull
        private Long sourceAccountId;

        @NotNull
        private Long targetAccountId;

        @NotNull
        @DecimalMin(value = "0.01")
        private BigDecimal amount;

        @NotNull
        private LocalDateTime timestamp;

}
