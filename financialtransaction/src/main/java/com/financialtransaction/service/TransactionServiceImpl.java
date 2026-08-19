package com.financialtransaction.service;

import com.financialtransaction.dto.TransactionRequestDTO;
import com.financialtransaction.entity.Account;
import com.financialtransaction.entity.Transaction;
import com.financialtransaction.entity.TransactionStatus;
import com.financialtransaction.repository.AccountRepository;
import com.financialtransaction.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Async("transactionExecutor")
    public void processTransactions(String batchId,
                                    List<TransactionRequestDTO> transactions) {

        log.info("Batch Started : {}", batchId);

        List<TransactionRequestDTO> validTransactions =
                validateTransactions(transactions);

        List<TransactionRequestDTO> uniqueTransactions =
                removeDuplicates(validTransactions);

        Map<Long, List<TransactionRequestDTO>> grouped =
                uniqueTransactions.stream()
                        .collect(Collectors.groupingBy(TransactionRequestDTO::getSourceAccountId));

        grouped.values().forEach(list ->
                list.forEach(this::processSingleTransaction));

        log.info("Batch Completed : {}", batchId);
    }

    private List<TransactionRequestDTO> validateTransactions(
            List<TransactionRequestDTO> transactions) {

        return transactions.stream()
                .filter(t -> t.getAmount().compareTo(BigDecimal.ZERO) > 0)
                .filter(t -> !Objects.equals(t.getSourceAccountId(),
                        t.getTargetAccountId()))
                .toList();
    }

    private List<TransactionRequestDTO> removeDuplicates(
            List<TransactionRequestDTO> transactions) {

        List<TransactionRequestDTO> result = new ArrayList<>();

        for (TransactionRequestDTO current : transactions) {

            boolean duplicate = result.stream().anyMatch(existing ->

                    existing.getSourceAccountId().equals(current.getSourceAccountId())
                            &&
                            existing.getTargetAccountId().equals(current.getTargetAccountId())
                            &&
                            existing.getAmount().compareTo(current.getAmount()) == 0
                            &&
                            Math.abs(Duration.between(
                                    existing.getTimestamp(),
                                    current.getTimestamp()).getSeconds()) <= 2
            );

            if (!duplicate) {
                result.add(current);
            }
        }

        return result;
    }

    @Transactional
    public void processSingleTransaction(TransactionRequestDTO dto) {

        Transaction transaction = new Transaction();

        transaction.setSourceAccountId(dto.getSourceAccountId());
        transaction.setTargetAccountId(dto.getTargetAccountId());
        transaction.setAmount(dto.getAmount());
        transaction.setTimestamp(LocalDateTime.now());

        try {

            Long firstLock =
                    Math.min(dto.getSourceAccountId(),
                            dto.getTargetAccountId());

            Long secondLock =
                    Math.max(dto.getSourceAccountId(),
                            dto.getTargetAccountId());

            accountRepository.findAccountForUpdate(firstLock)
                    .orElseThrow();

            accountRepository.findAccountForUpdate(secondLock)
                    .orElseThrow();

            Account source =
                    accountRepository.findById(dto.getSourceAccountId())
                            .orElseThrow();

            Account target =
                    accountRepository.findById(dto.getTargetAccountId())
                            .orElseThrow();

            if (source.getBalance().compareTo(dto.getAmount()) < 0) {

                transaction.setStatus(TransactionStatus.FAILED);
                transaction.setFailureReason("Insufficient Balance");

                transactionRepository.save(transaction);

                return;
            }

            source.setBalance(
                    source.getBalance().subtract(dto.getAmount()));

            target.setBalance(
                    target.getBalance().add(dto.getAmount()));

            accountRepository.save(source);
            accountRepository.save(target);

            transaction.setStatus(TransactionStatus.SUCCESS);

        } catch (Exception e) {

            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setFailureReason(e.getMessage());

        }

        transactionRepository.save(transaction);
    }
}
