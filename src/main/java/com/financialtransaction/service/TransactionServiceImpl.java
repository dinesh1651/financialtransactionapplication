package com.financialtransaction.service;

import com.financialtransaction.dto.TransactionRequestDTO;
import com.financialtransaction.entity.Account;
import com.financialtransaction.entity.Transaction;
import com.financialtransaction.entity.TransactionStatus;
import com.financialtransaction.exception.NoTransactionAvailableException;
import com.financialtransaction.repository.AccountRepository;
import com.financialtransaction.repository.TransactionRepository;
import jakarta.annotation.PostConstruct;
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
    private final TransactionProcessorService transactionProcessorService;

    private final List<Account> accounts = new ArrayList<>();

    @PostConstruct
    public void init() {

        Account account1 = Account.builder()
                .id(1L)
                .ownerName("John")
                .balance(new BigDecimal("10000.00"))
                .currency("INR")
                .build();

        Account account2 = Account.builder()
                .id(2L)
                .ownerName("David")
                .balance(new BigDecimal("5000.00"))
                .currency("INR")
                .build();

        Account account3 = Account.builder()
                .id(3L)
                .ownerName("Dinesh")
                .balance(new BigDecimal("20000.00"))
                .currency("INR")
                .build();

        accounts.add(account1);
        accounts.add(account2);
        accounts.add(account3);
    }

    @Override
    @Async("transactionExecutor")
    public void processTransactions(String batchId, List<TransactionRequestDTO> transactions) {

        log.info("Batch Started : {}", batchId);

        List<TransactionRequestDTO> validTransactions = validateTransactions(transactions);

        List<TransactionRequestDTO> uniqueTransactions = removeDuplicates(validTransactions);

        Map<Long, List<TransactionRequestDTO>> grouped = uniqueTransactions.stream()
                        .collect(Collectors.groupingBy(TransactionRequestDTO::getSourceAccountId));

        grouped.values().forEach(list ->
                list.forEach(transactionProcessorService::processSingleTransaction));

        log.info("Batch Completed : {}", batchId);
    }

    private List<TransactionRequestDTO> validateTransactions(
            List<TransactionRequestDTO> transactions) {
        log.info("Batch Completed............ : {}", transactions);
        List<TransactionRequestDTO> validTransactions = transactions.stream()
                .filter(t -> t.getAmount().compareTo(BigDecimal.ZERO) > 0)
                .filter(t -> !Objects.equals(t.getSourceAccountId(),
                        t.getTargetAccountId())).toList();

        if (validTransactions.isEmpty()) {
            throw new NoTransactionAvailableException(
                    "Please change target account");
        }
        return validTransactions;
    }

    private List<TransactionRequestDTO> removeDuplicates(List<TransactionRequestDTO> transactions) {

        List<TransactionRequestDTO> result = new ArrayList<>();

        for (TransactionRequestDTO current : transactions) {

            boolean duplicate = result.stream().anyMatch(existing ->
                    existing.getSourceAccountId().equals(current.getSourceAccountId())
                            && existing.getTargetAccountId().equals(current.getTargetAccountId())
                            && existing.getAmount().compareTo(current.getAmount()) == 0
                            && Math.abs(Duration.between(existing.getTimestamp(),
                                    current.getTimestamp()).getSeconds()) <= 2
            );

            if (!duplicate) {
                result.add(current);
            }
        }

        return result;
    }

    @Override
    public List<Transaction> getTransactionsByAccountId(Long accountId,
                                                        Optional<LocalDateTime> from,
                                                        Optional<LocalDateTime> to) {
        accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found:" + accountId));

        if(from.isPresent() && to.isPresent()){
            return transactionRepository
                    .findTransactionsByAccountAndDateRange(accountId, from.get(), to.get());
        }
        return transactionRepository.findTransactionHistory(accountId);
    }
}
