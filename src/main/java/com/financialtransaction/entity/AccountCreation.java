package com.financialtransaction.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "accounts_creations",
uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_account_number",
                columnNames = "account_number"
        ),
        @UniqueConstraint(
                name = "uk_contact_number",
                columnNames = "contact_number"
        ),
        @UniqueConstraint(
                name = "uk_pan_number",
                columnNames = "pan_number"
        )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountCreation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String middleName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true, updatable = true)
    @Pattern(regexp = "^[6-9]\\d{9}$",
            message = "Invalid mobile number")
    private String contactNumber;

    @Column(nullable = false)
    @Email(message = "Invalid email address")
    private String email;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, unique = true, updatable = false)
    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]$",
            message = "Invalid PAN number")
    private String panNumber;

    @Column(name = "account_number", nullable = false, unique = true, length = 12)
    private String accountNumber;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @OneToMany(mappedBy = "accountCreation",
                cascade = CascadeType.ALL,
                orphanRemoval = true)
    @JsonManagedReference
    private List<Account> accounts;
}
