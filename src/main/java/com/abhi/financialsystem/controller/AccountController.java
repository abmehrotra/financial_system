package com.abhi.financialsystem.controller;


import com.abhi.financialsystem.dto.AccountRequest;
import com.abhi.financialsystem.dto.AccountResponse;
import com.abhi.financialsystem.model.Account;
import com.abhi.financialsystem.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest request) {
        log.info("Received request to create account for documentNumber={}", request.documentNumber());

        Account account = accountService.createAccount(request.documentNumber());

        log.info("Successfully created account. accountId={} documentNumber={}",
                account.getAccountId(), account.getDocumentNumber());

        return ResponseEntity.ok(AccountResponse.builder()
                .accountId(account.getAccountId())
                .documentNumber(account.getDocumentNumber())
                .build());
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long accountId) {
        log.info("Fetching account with accountId={}", accountId);

        Account account = accountService.getAccount(accountId);

        log.info("Retrieved account. accountId={} documentNumber={}",
                account.getAccountId(), account.getDocumentNumber());

        return ResponseEntity.ok(AccountResponse.builder()
                .accountId(account.getAccountId())
                .documentNumber(account.getDocumentNumber())
                .build());
    }
}
