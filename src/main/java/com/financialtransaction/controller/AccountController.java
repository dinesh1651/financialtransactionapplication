package com.financialtransaction.controller;

import com.financialtransaction.dto.AccountRequestDTO;
import com.financialtransaction.entity.Account;
import com.financialtransaction.entity.Transaction;
import com.financialtransaction.repository.AccountRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody @Valid AccountRequestDTO request){
        Account account = Account.builder()
                .ownerName(request.getOwnerName())
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



}
