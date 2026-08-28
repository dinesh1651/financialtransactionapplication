package com.financialtransaction.service;

import com.financialtransaction.dto.TransactionRequestDTO;
import com.financialtransaction.entity.Account;
import com.financialtransaction.entity.Transaction;
import com.financialtransaction.entity.TransactionStatus;
import com.financialtransaction.repository.AccountRepository;
import com.financialtransaction.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionProcessorService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void processSingleTransaction(TransactionRequestDTO dto) {

        Transaction transaction = new Transaction();

        transaction.setSourceAccountId(dto.getSourceAccountId());
        transaction.setTargetAccountId(dto.getTargetAccountId());
        transaction.setAmount(dto.getAmount());
        transaction.setTimestamp(dto.getTimestamp());

        try {

            Long firstLock = Math.min(dto.getSourceAccountId(),dto.getTargetAccountId());

            Long secondLock = Math.max(dto.getSourceAccountId(), dto.getTargetAccountId());

            Account firstAccount = accountRepository.findAccountForUpdate(firstLock)
                            .orElseThrow(() -> new RuntimeException("Account not found: " + firstLock));

            Account secondAccount = accountRepository.findAccountForUpdate(secondLock)
                    .orElseThrow(() -> new RuntimeException("Account not found: " + secondLock));

            Account source;
            Account target;

            if (dto.getSourceAccountId().equals(firstLock)) {
                source = firstAccount;
                target = secondAccount;
            } else {
                source = secondAccount;
                target = firstAccount;
            }

            if (source.getBalance().compareTo(dto.getAmount()) < 0) {

                transaction.setStatus(TransactionStatus.FAILED);
                transaction.setFailureReason("Insufficient Balance");

                transactionRepository.save(transaction);
            }

            source.setBalance(source.getBalance().subtract(dto.getAmount()));
            target.setBalance(target.getBalance().add(dto.getAmount()));

            accountRepository.save(source);
            accountRepository.save(target);

            transaction.setStatus(TransactionStatus.SUCCESS);
//            transaction.setTimestamp(LocalDateTime.now());
            transactionRepository.save(transaction);

        } catch (Exception e) {

            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setFailureReason(e.getMessage());

            transactionRepository.save(transaction);
        }
    }
}
