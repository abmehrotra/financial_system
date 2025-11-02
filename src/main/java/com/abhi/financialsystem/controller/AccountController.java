package com.abhi.financialsystem.controller;


import com.abhi.financialsystem.dto.AccountRequest;
import com.abhi.financialsystem.dto.AccountResponse;
import com.abhi.financialsystem.model.Account;
import com.abhi.financialsystem.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest request) {
        Account account = accountService.createAccount(request.documentNumber());
        return ResponseEntity.ok(AccountResponse.builder()
                .accountId(account.getAccountId())
                .documentNumber(account.getDocumentNumber())
                .build());
    }

    @GetMapping
    public ResponseEntity<AccountResponse> getAccount(@RequestParam Long accountId) {
        Account account = accountService.getAccount(accountId);
        return ResponseEntity.ok(AccountResponse.builder()
                .accountId(account.getAccountId())
                .documentNumber(account.getDocumentNumber())
                .build());
    }
}
