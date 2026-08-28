package com.financialtransaction.service;

import com.financialtransaction.dto.DepositDTO;
import com.financialtransaction.dto.WithdrawDTO;
import com.financialtransaction.entity.Account;
import com.financialtransaction.entity.AccountCreation;
import jakarta.validation.Valid;

public interface AccountService {

//    Account updateBalance(String accountNumber, DepositDTO request);

    Account withdrawBalance(String accountNumber, @Valid WithdrawDTO request);

    Account depositBalance(String accountNumber, @Valid DepositDTO request);
}
