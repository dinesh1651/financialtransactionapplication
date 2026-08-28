package com.financialtransaction.service;

import com.financialtransaction.dto.AccountCreationDTO;
import com.financialtransaction.entity.AccountCreation;
import com.financialtransaction.repository.AccountCreationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class AccountCreationServiceImpl implements AccountCreationService{

    @Autowired
    private AccountCreationRepository accountCreationRepository;

    @Override
    public AccountCreation accountCreation(AccountCreationDTO request) {
        String accountNumber = generateAccountNumber();

        AccountCreation accountCreation = new AccountCreation();
        accountCreation.setFirstName(request.getFirstName());
        accountCreation.setMiddleName(request.getMiddleName());
        accountCreation.setLastName(request.getLastName());
        accountCreation.setContactNumber(request.getContactNumber());
        accountCreation.setEmail(request.getEmail());
        accountCreation.setAddress(request.getAddress());
        accountCreation.setPanNumber(request.getPanNumber());
        accountCreation.setAccountNumber(accountNumber);

        return accountCreationRepository.save(accountCreation);
    }

        private String generateAccountNumber() {

        String datePrefix =
                LocalDate.now().format(
                        DateTimeFormatter.ofPattern("yyyyMMdd"));

        Optional<AccountCreation> latestAccount =
                accountCreationRepository
                        .findTopByAccountNumberStartingWithOrderByAccountNumberDesc(
                                datePrefix);

        int nextSerial = 1;

        if (latestAccount.isPresent()) {

            String lastAccountNumber =
                    latestAccount.get().getAccountNumber();

            String serialPart =
                    lastAccountNumber.substring(8);

            nextSerial = Integer.parseInt(serialPart) + 1;
        }

        return datePrefix + String.format("%04d", nextSerial);
    }

    @Override
    public AccountCreation getAccountDetails(String accountNumber) {
        return accountCreationRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account Number Not Exists" + " " + accountNumber));
    }

    @Override
    public AccountCreation updateAccountDetails(String accountNumber, AccountCreationDTO request) {
        AccountCreation accountCreation = accountCreationRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account Number Not Exists:" + " " + accountNumber));

        System.out.println("Before contact number");
        System.out.println(accountCreation.getContactNumber());

        accountCreation.setFirstName(request.getFirstName());
        accountCreation.setMiddleName(request.getMiddleName());
        accountCreation.setLastName(request.getLastName());
        accountCreation.setAddress(request.getAddress());
        accountCreation.setPanNumber(request.getPanNumber());
        accountCreation.setContactNumber(request.getContactNumber());
        accountCreation.setEmail(request.getEmail());

        System.out.println("After contact Number");
        System.out.println(request.getContactNumber());

        return accountCreationRepository.save(accountCreation);
    }

    @Override
    @Transactional
    public void deleteAccount(String accountNumber) {
        AccountCreation deleteAccount = accountCreationRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account Number Not Exists: " + " " + accountNumber));

        accountCreationRepository.delete(deleteAccount);
    }


}
