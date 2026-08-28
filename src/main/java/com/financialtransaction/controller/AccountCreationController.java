package com.financialtransaction.controller;

import com.financialtransaction.dto.AccountCreationDTO;
import com.financialtransaction.entity.AccountCreation;
import com.financialtransaction.repository.AccountCreationRepository;
import com.financialtransaction.service.AccountCreationService;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AccountCreationController {

//    @Autowired
//    private AccountCreationRepository accountCreationRepository;

    @Autowired
    private AccountCreationService accountCreationService;

    @PostMapping("/createAccount")
    public ResponseEntity<AccountCreation> createAccount(@RequestBody @Valid AccountCreationDTO request){
//        AccountCreation accountCreation = AccountCreation.builder()
//                .firstName(request.getFirstName())
//                .middleName(request.getMiddleName())
//                .lastName(request.getLastName())
//                .contactNumber(request.getContactNumber())
//                .email(request.getEmail())
//                .address(request.getAddress())
//                .panNumber(request.getPanNumber())
//                .build();

        AccountCreation newAccountCreation = accountCreationService.accountCreation(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newAccountCreation);
    }

    @GetMapping("/account_number/{accountNumber}")
    public ResponseEntity<AccountCreation> getAccountDetails(@PathVariable String accountNumber){
        AccountCreation accountDetails = accountCreationService.getAccountDetails(accountNumber);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountDetails);
    }

    @PutMapping("/account_number/{accountNumber}")
    public ResponseEntity<AccountCreation> updateAccountDetails(@PathVariable String accountNumber,
                                                             @Valid @RequestBody AccountCreationDTO request){
        AccountCreation updateAccountDetails = accountCreationService
                .updateAccountDetails(accountNumber,request);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(updateAccountDetails);
    }

    @DeleteMapping("/delete_account/{accountNumber}")
    public ResponseEntity<String> deleteAccount(@PathVariable String accountNumber){
        accountCreationService.deleteAccount(accountNumber);

        return ResponseEntity
                .ok("Account deleted successfully");
    }
}
