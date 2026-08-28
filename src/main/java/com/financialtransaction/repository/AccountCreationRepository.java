package com.financialtransaction.repository;

import com.financialtransaction.entity.AccountCreation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountCreationRepository extends JpaRepository<AccountCreation, Long> {

    Optional<AccountCreation> findTopByAccountNumberStartingWithOrderByAccountNumberDesc(
            String prefix);

    Optional<AccountCreation> findByAccountNumber(String accountNumber);

    void deleteByAccountNumber(String accountNumber);
}
