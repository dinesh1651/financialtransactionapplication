package com.financialtransaction.controller;

import com.financialtransaction.dto.BatchResponseDTO;
import com.financialtransaction.dto.TransactionRequestDTO;
import com.financialtransaction.entity.Transaction;
import com.financialtransaction.exception.NoTransactionAvailableException;
import com.financialtransaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

    @RestController
    @RequestMapping("/api/v1/transactions")
    @RequiredArgsConstructor
    public class TransactionController {

        private final TransactionService transactionService;

        @PostMapping("/process")
        public ResponseEntity<BatchResponseDTO> processTransactions(
                @RequestBody @Valid List<TransactionRequestDTO> transactions) {

            for (TransactionRequestDTO transaction : transactions) {

                if (transaction.getSourceAccountId()
                        .equals(transaction.getTargetAccountId())) {

                    throw new NoTransactionAvailableException(
                            "Source and target account must be different"
                    );
                }
            }

            String batchId = UUID.randomUUID().toString();

            transactionService.processTransactions(batchId, transactions);

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(new BatchResponseDTO(
                            batchId,
                            "Batch accepted for processing"
                    ));
        }
//        @GetMapping("/history/{accountId}")
//        public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable Long accountId,
//                                                                           @RequestParam LocalDateTime from,
//                                                                           @RequestParam LocalDateTime to){
//            return ResponseEntity.ok(transactionService.getTransactionsByAccountId(accountId, from, to));
//        }
        @GetMapping("/history/{accountId}")
        public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable Long accountId,
                                                                       @RequestParam(required = false) Optional<LocalDateTime> from,
                                                                       @RequestParam(required = false) Optional<LocalDateTime> to){
            return ResponseEntity.ok(transactionService.getTransactionsByAccountId(accountId, from, to));
        }

        @GetMapping("/transactionGreaterThanThousand")
        public ResponseEntity<List<Transaction>> getTransactionsGreaterThanThousand(){
            return ResponseEntity.ok(transactionService.getTransactionGreaterThanThousand());
        }
}
