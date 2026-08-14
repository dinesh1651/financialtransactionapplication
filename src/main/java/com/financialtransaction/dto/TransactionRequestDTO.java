package com.financialtransaction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

    @Data
    public class TransactionRequestDTO {

        @NotNull(message = "Source account ID is required")
        private Long sourceAccountId;

        @NotNull(message = "Target account ID is required")
        private Long targetAccountId;

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
        private BigDecimal amount;

        @NotNull(message = "Timestamp is required")
        private LocalDateTime timestamp;

}
