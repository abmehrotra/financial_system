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

import java.math.BigDecimal;
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

    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        account = Account.builder()
                .accountId(1L)
                .documentNumber("1234")
                .balance(0)
                .limit(1000)
                .build();
    }

    //PURCHASE (1) - $1001,00 -> NOK
    //PURCHASE (1)  - $ 1000,00 -> OK
    //PURCHASE (1)  - $ 1,00 -> NOK
    //PAYMENT (4) - $ 1000,00 -> OK
    //PURCHASE (1)  - $ 1,00 -> OK
    //PURCHASE (1)  - $ 1000,00 -> NOK
    //PURCHASE (1)  - $ 999,00 -> NOK
    @Test
    void purchaseWithinLimit_shouldBeAllowedAndDecreaseBalance() {
        OperationType purchase = OperationType.builder().operationTypeId(1L).description("PURCHASE").build();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(operationTypeRepository.findById(1L)).thenReturn(Optional.of(purchase));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        Transaction trnasc = transactionService.createTransaction(1L, 1L, 1000.00);

        assertThat(trnasc.getAmount()).isEqualTo(-1000);
        assertThat((account.getBalance())).isEqualTo(-1000);
    }

    @Test
    void purchaseBeyondLimit_shouldRespectLimit(){
        OperationType purchase = OperationType.builder().operationTypeId(1L).description("PURCHASE").build();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(operationTypeRepository.findById(1L)).thenReturn(Optional.of(purchase));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        transactionService.createTransaction(1L, 1L, 1000.00);
        assertThat(account.getBalance()).isEqualTo(-1000);

        assertThatThrownBy(() -> transactionService.createTransaction(1L, 1L, 1.00))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient Limit");
    }

    @Test
    void purchaseBeyondLimit_showThrowError(){
        OperationType purchase = OperationType.builder().operationTypeId(1L).description("PURCHASE").build();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(operationTypeRepository.findById(1L)).thenReturn(Optional.of(purchase));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        assertThatThrownBy(() -> transactionService.createTransaction(1L, 1L, 1001.00))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient Limit");
    }

    @Test
    void multipleTransactions_shouldRespectLimit(){
        OperationType purchase = OperationType.builder().operationTypeId(1L).description("PURCHASE").build();
        OperationType payment = OperationType.builder().operationTypeId(4L).description("PAYMENT").build();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(operationTypeRepository.findById(1L)).thenReturn(Optional.of(purchase));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        transactionService.createTransaction(1L, 1L, 1000.00);
        assertThat(account.getBalance()).isEqualTo(-1000);

        assertThatThrownBy(() -> transactionService.createTransaction(1L, 1L, 1.00))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient Limit");

        when(operationTypeRepository.findById(4L)).thenReturn(Optional.of(payment));
        transactionService.createTransaction(1L, 4L, 1000.00);
        assertThat(account.getBalance()).isEqualTo(0);

        transactionService.createTransaction(1L, 1L, 1.00);
        assertThat(account.getBalance()).isEqualTo(-1); //-1


        assertThatThrownBy(() -> transactionService.createTransaction(1L, 1L, 1000.00))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient Limit");
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
