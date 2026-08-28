package com.financialtransaction.service;

import com.financialtransaction.dto.DepositDTO;
import com.financialtransaction.dto.WithdrawDTO;
import com.financialtransaction.entity.Account;
import com.financialtransaction.entity.AccountCreation;
import com.financialtransaction.repository.AccountCreationRepository;
import com.financialtransaction.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountRepository accountRepository;
    private AccountCreationRepository accountCreationRepository;

//    @Transactional
//    @Override
//    public Account updateBalance(String accountNumber, DepositDTO request) {
//        Account account = accountRepository.findByAccountCreation_AccountNumber(accountNumber)
//                .orElseThrow(() -> new RuntimeException("Account Number Not Exists"));
//
//        BigDecimal currentBalance = account.getBalance();
//        BigDecimal depositBalance = request.getBalance();
//        BigDecimal newBalance = currentBalance.add(depositBalance);
//
//        account.setBalance(newBalance);
//        return accountRepository.save(account);
//    }

    @Override
    @Transactional
    public Account withdrawBalance(String accountNumber, WithdrawDTO request) {
        Account account = accountRepository.findByAccountCreation_AccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account Number Not Exists"));

        BigDecimal availableBalance = account.getBalance();
        BigDecimal withdrawBalance = request.getBalance();
        if (withdrawBalance.compareTo(availableBalance) > 0){
            throw new RuntimeException("Insufficient balance");
        }
        BigDecimal remainingBalance = availableBalance.subtract(withdrawBalance);
        account.setBalance(remainingBalance);
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account depositBalance(String accountNumber, DepositDTO request) {
        Account account = accountRepository.findByAccountCreation_AccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account Number Not Found:" + " " + accountNumber));
        BigDecimal avlBalance = account.getBalance();
        BigDecimal depositBalance = request.getBalance();
        BigDecimal totalBalance = avlBalance.add(depositBalance);
        account.setBalance(totalBalance);
        return accountRepository.save(account);
    }
}
