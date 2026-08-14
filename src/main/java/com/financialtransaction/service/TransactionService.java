package com.financialtransaction.service;


import com.financialtransaction.dto.TransactionRequestDTO;
import com.financialtransaction.entity.Transaction;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionService {

        @Async("transactionExecutor")
        void processTransactions(String batchId, List<TransactionRequestDTO> transactions);

        List<Transaction> getTransactionsByAccountId(Long accountId, Optional<LocalDateTime> from, Optional<LocalDateTime> to);
}
