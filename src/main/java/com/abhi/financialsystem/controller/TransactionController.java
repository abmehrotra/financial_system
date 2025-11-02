package com.abhi.financialsystem.controller;

import com.abhi.financialsystem.dto.TransactionRequest;
import com.abhi.financialsystem.dto.TransactionResponse;
import com.abhi.financialsystem.model.Transaction;
import com.abhi.financialsystem.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest request) {
        log.info("Creating transaction for accountId={} operationTypeId={} amount={}",
                request.accountId(), request.operationTypeId(), request.amount());

        Transaction transaction = transactionService.createTransaction(
                request.accountId(),
                request.operationTypeId(),
                request.amount()
        );

        log.info("Transaction created successfully. accountId={} transactionId={} operationType={} amount={}",
                request.accountId(), transaction.getTransactionId(), transaction.getOperationType(), transaction.getTransactionId());

        return ResponseEntity.ok(TransactionResponse.builder()
                .transactionId(transaction.getTransactionId())
                .accountId(transaction.getAccount().getAccountId())
                .operationTypeId(transaction.getOperationType().getOperationTypeId())
                .amount(transaction.getAmount())
                .eventDate(transaction.getEventDate())
                .build());
    }
}
