package com.financialtransaction.service;

import com.financialtransaction.dto.AccountCreationDTO;
import com.financialtransaction.entity.AccountCreation;
import org.springframework.stereotype.Service;


public interface AccountCreationService {

    AccountCreation accountCreation(AccountCreationDTO request);

    AccountCreation getAccountDetails(String accountNumber);

    AccountCreation updateAccountDetails(String accountNumber, AccountCreationDTO request);

    void deleteAccount(String accountNumber);
}
