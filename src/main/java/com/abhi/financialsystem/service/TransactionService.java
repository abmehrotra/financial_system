package com.abhi.financialsystem.service;

import com.abhi.financialsystem.model.Transaction;

public interface TransactionService {
    Transaction createTransaction(Long accountId, Long operationTypeId, Double amount);
}
