package com.abhi.financialsystem.service;

import com.abhi.financialsystem.exception.ResourceConflictException;
import com.abhi.financialsystem.model.Account;
import com.abhi.financialsystem.repository.AccountRepository;
import com.abhi.financialsystem.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;


    @InjectMocks
    private AccountServiceImpl accountService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccount_whenNotExists_shouldCreate() {
        when(accountRepository.findByDocumentNumber(anyString())).thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Account created = accountService.createAccount("12345678900");

        assertThat(created).isNotNull();
        assertThat(created.getDocumentNumber()).isEqualTo("12345678900");
    }

    @Test
    void createAccount_whenAlreadyExists_shouldThrow() {
        when(accountRepository.findByDocumentNumber("123")).thenReturn(Optional.of(
                Account.builder().accountId(1L).documentNumber("123").build()
        ));

        assertThatThrownBy(() -> accountService.createAccount("123"))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessageContaining("Account with this document number already exists");
    }

    @Test
    void getAccount_whenExists_shouldReturnAccountDetails() {
        Account account = Account.builder().accountId(1L).documentNumber("123").build();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Account found = accountService.getAccount(1L);

        assertThat(found).isNotNull();
        assertThat(found.getAccountId()).isEqualTo(1L);
        assertThat(found.getDocumentNumber()).isEqualTo("123");
    }

    @Test
    void getAccount_whenDoesNotExists_shouldThrow() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccount(99L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Account not found");
    }
}
