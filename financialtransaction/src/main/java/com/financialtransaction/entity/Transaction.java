package com.financialtransaction.entity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private Long sourceAccountId;

        @Column(nullable = false)
        private Long targetAccountId;

        @Column(nullable = false, precision = 19, scale = 2)
        private BigDecimal amount;

        @Enumerated(EnumType.STRING)
        private TransactionStatus status;

        private LocalDateTime timestamp;

        private String failureReason;

}
