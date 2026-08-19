package com.financialtransaction.repository;

import com.financialtransaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
    Select   t From Transaction t WHERE (t.sourceAccountId= :accountId OR t.targetAccountId = :accountId)
    AND t.timestamp >= :from 
    AND t.timestamp <= :to
    ORDER BY t.timestamp DESC""")
    List<Transaction> findTransactionsByAccountAndDateRange(
            @Param("accountId") Long accountId,
            @Param("from")LocalDateTime from,
            @Param("to") LocalDateTime to
            );

    @Query(""" 
    SELECT t FROM Transaction t WHERE t.sourceAccountId = :accountId
    OR t.targetAccountId = :accountId
    ORDER BY t.timestamp DESC""")
    List<Transaction> findTransactionHistory(@Param("accountId") Long accountId);

    @Query("""
    SELECT t FROM Transaction t WHERE amount>=:amount AND status="SUCCESS" """)
    List<Transaction> findTransactionGreaterThanThousand(BigDecimal amount);
}
