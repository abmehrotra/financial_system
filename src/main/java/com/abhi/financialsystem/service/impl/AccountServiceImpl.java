package com.abhi.financialsystem.service.impl;

import com.abhi.financialsystem.exception.ResourceConflictException;
import com.abhi.financialsystem.model.Account;
import com.abhi.financialsystem.repository.AccountRepository;
import com.abhi.financialsystem.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public Account createAccount(String documentNumber) {
        if (accountRepository.findByDocumentNumber(documentNumber).isPresent()) {
            throw new ResourceConflictException("Account with this document number already exists");
        }

        return accountRepository.save(
                Account.builder()
                        .documentNumber(documentNumber)
                        .build()
        );
    }

    @Override
    public Account getAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("Account not found"));
    }
}
