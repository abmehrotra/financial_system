package com.abhi.financialsystem.service;

import com.abhi.financialsystem.model.Account;
import com.abhi.financialsystem.model.OperationType;
import com.abhi.financialsystem.model.Transaction;
import com.abhi.financialsystem.repository.AccountRepository;
import com.abhi.financialsystem.repository.OperationTypeRepository;
import com.abhi.financialsystem.repository.TransactionRepository;
import com.abhi.financialsystem.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TransactionServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private OperationTypeRepository operationTypeRepository;
    @Mock
    private TransactionRepository transactionRepository;


    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTransaction_payment_shouldBePositive() {
        Account acc = Account.builder().accountId(1L).documentNumber("123").build();
        OperationType payment = OperationType.builder().operationTypeId(4L).description("PAYMENT").build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(acc));
        when(operationTypeRepository.findById(4L)).thenReturn(Optional.of(payment));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        Transaction tx = transactionService.createTransaction(1L, 4L, 100.0);

        assertThat(tx).isNotNull();
        assertThat(tx.getAmount()).isEqualTo(100.0);
    }

    @ParameterizedTest
    @CsvSource({
            "PURCHASE, 50.0, -50.0",
            "INSTALLMENT PURCHASE, 100.0, -100.0",
            "WITHDRAWAL, 25.0, -25.0"
    })
    void createTransaction_shouldHandleDifferentNegativeAmounts(
            String description, double amount, double expectedAmount) {

        // Arrange
        Account acc = Account.builder().accountId(1L).documentNumber("123").build();
        OperationType op = OperationType.builder().operationTypeId(1L).description(description).build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(acc));
        when(operationTypeRepository.findById(1L)).thenReturn(Optional.of(op));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Transaction tx = transactionService.createTransaction(1L, 1L, amount);

        // Assert
        assertThat(tx).isNotNull();
        assertThat(tx.getAmount())
                .as("Incorrect amount for operation type: " + description)
                .isEqualTo(expectedAmount);
    }

    @Test
    void createTransaction_invalidAccount_shouldThrow() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.createTransaction(99L, 1L, 100.0))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Invalid account ID");
    }

    @Test
    void createTransaction_invalidOperationType_shouldThrow() {
        Account acc = Account.builder().accountId(1L).documentNumber("123").build();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(acc));
        when(operationTypeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.createTransaction(1L, 99L, 100.0))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Invalid operation type ID");
    }
}
