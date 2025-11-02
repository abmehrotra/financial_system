package com.abhi.financialsystem.service;

import com.abhi.financialsystem.model.Account;

import java.util.Optional;

public interface AccountService {
    Account createAccount(String documentNumber);
    Account getAccount(Long accountId);
}
