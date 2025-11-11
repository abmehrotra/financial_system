package com.abhi.financialsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;


    @Column(name = "document_number", nullable = false, unique = true)
    private String documentNumber;

    @Column
    private double balance = 0.0;

    @Column
    private double limit = 1000.00;

}
