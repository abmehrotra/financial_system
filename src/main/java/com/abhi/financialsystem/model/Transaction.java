package com.abhi.financialsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;


    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id")
    private Account account;


    @ManyToOne(optional = false)
    @JoinColumn(name = "operation_type_id")
    private OperationType operationType;


    @Column(nullable = false)
    private Double amount;


    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
}
