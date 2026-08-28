package com.financialtransaction.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AccountCreationDTO {
    @NotBlank
    private String firstName;

    @Column(nullable = false)
    @NotBlank
    private String middleName;

    @Column(nullable = false)
    @NotBlank
    private String lastName;

    @Column(nullable = false, updatable = true, unique = true)
    @Pattern(regexp = "^[6-9]\\d{9}$",
            message = "Invalid mobile number")
    @NotBlank(message = "Contact Number is required")
    private String contactNumber;

    @Column(nullable = false)
    @Email(message = "Invalid email address")
    private String email;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, updatable = false, unique = true)
    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]$",
            message = "Invalid PAN number")
    @NotBlank
    private String panNumber;

//    @NotBlank
    private String accountNumber;
}
