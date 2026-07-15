package com.financialtransaction.service;


import com.financialtransaction.dto.TransactionRequestDTO;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

    public interface TransactionService {

        @Async("transactionExecutor")
        void processTransactions(String batchId,
                                 List<TransactionRequestDTO> transactions);
}
