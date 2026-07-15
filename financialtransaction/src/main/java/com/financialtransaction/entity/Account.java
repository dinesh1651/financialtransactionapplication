package com.financialtransaction.entity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String ownerName;

        @Column(nullable = false, precision = 19, scale = 2)
        private BigDecimal balance;

        @Column(nullable = false)
        private String currency;

}
