package com.abhi.financialsystem.service.impl;

import com.abhi.financialsystem.model.Account;
import com.abhi.financialsystem.model.OperationType;
import com.abhi.financialsystem.model.Transaction;
import com.abhi.financialsystem.repository.AccountRepository;
import com.abhi.financialsystem.repository.OperationTypeRepository;
import com.abhi.financialsystem.repository.TransactionRepository;
import com.abhi.financialsystem.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {


    private final AccountRepository accountRepository;
    private final OperationTypeRepository operationTypeRepository;
    private final TransactionRepository transactionRepository;


    @Override
    @Transactional
    public Transaction createTransaction(Long accountId, Long operationTypeId, Double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("Invalid account ID"));


        OperationType operationType = operationTypeRepository.findById(operationTypeId)
                .orElseThrow(() -> new NoSuchElementException("Invalid operation type ID"));


        double signedAmount = operationType.getDescription().equalsIgnoreCase("PAYMENT") ? Math.abs(amount) : -Math.abs(amount);


        Transaction transaction = Transaction.builder()
                .account(account)
                .operationType(operationType)
                .amount(signedAmount)
                .eventDate(LocalDateTime.now())
                .build();


        return transactionRepository.save(transaction);
    }
}
