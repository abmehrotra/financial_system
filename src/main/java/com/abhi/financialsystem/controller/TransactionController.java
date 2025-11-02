package com.abhi.financialsystem.controller;

import com.abhi.financialsystem.dto.TransactionRequest;
import com.abhi.financialsystem.dto.TransactionResponse;
import com.abhi.financialsystem.model.Transaction;
import com.abhi.financialsystem.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody TransactionRequest request) {
        Transaction transaction = transactionService.createTransaction(
                request.accountId(),
                request.operationTypeId(),
                request.amount()
        );
        return ResponseEntity.ok(TransactionResponse.builder()
                .transactionId(transaction.getTransactionId())
                .accountId(transaction.getAccount().getAccountId())
                .operationTypeId(transaction.getOperationType().getOperationTypeId())
                .amount(transaction.getAmount())
                .eventDate(transaction.getEventDate())
                .build());
    }
}
