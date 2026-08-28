package com.financialtransaction.controller;

import com.financialtransaction.dto.AccountRequestDTO;
import com.financialtransaction.dto.DepositDTO;
import com.financialtransaction.dto.WithdrawDTO;
import com.financialtransaction.entity.Account;
import com.financialtransaction.entity.AccountCreation;
import com.financialtransaction.entity.Transaction;
import com.financialtransaction.repository.AccountRepository;
import com.financialtransaction.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody @Valid AccountRequestDTO request){
        Account account = Account.builder()
                .balance(request.getBalance())
                .currency(request.getCurrency())
                .build();

        Account savedAccount = accountRepository.save(account);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedAccount);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Account not found with id: " + id));

        return ResponseEntity.ok(account);
    }

    @PutMapping("/depositbalance/{accountNumber}")
    public ResponseEntity<Account> depositBalance(@PathVariable String accountNumber,
                                            @Valid @RequestBody DepositDTO request){
        Account depositBalance = accountService.depositBalance(accountNumber, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(depositBalance);
    }

    @PutMapping("/withdrawbalance/{accountNumber}")
    public ResponseEntity<Account> withdrawBalance(@PathVariable String accountNumber,
                                                   @Valid @RequestBody WithdrawDTO request){
        Account withdrawBalance = accountService.withdrawBalance(accountNumber, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(withdrawBalance);
    }
}
