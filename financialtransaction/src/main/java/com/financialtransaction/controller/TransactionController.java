package com.financialtransaction.controller;

import com.financialtransaction.dto.BatchResponseDTO;
import com.financialtransaction.dto.TransactionRequestDTO;
import com.financialtransaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

    @RestController
    @RequestMapping("/api/v1/transactions")
    @RequiredArgsConstructor
    public class TransactionController {

        private final TransactionService transactionService;

       /* @GetMapping("/test")
        public String test(){
            return "hi";
        }*/

        @PostMapping("/process")
        public ResponseEntity<BatchResponseDTO> processTransactions(
                @RequestBody @Valid List<TransactionRequestDTO> transactions) {

            String batchId = UUID.randomUUID().toString();

            transactionService.processTransactions(batchId, transactions);

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(new BatchResponseDTO(
                            batchId,
                            "Batch accepted for processing"
                    ));
        }
}
