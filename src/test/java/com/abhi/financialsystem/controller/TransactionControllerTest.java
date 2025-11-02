package com.abhi.financialsystem.controller;

import com.abhi.financialsystem.dto.TransactionRequest;
import com.abhi.financialsystem.model.Account;
import com.abhi.financialsystem.model.OperationType;
import com.abhi.financialsystem.model.Transaction;
import com.abhi.financialsystem.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTransaction_shouldReturnOk() throws Exception {
        Account acc = Account.builder().accountId(1L).documentNumber("123").build();
        OperationType op = OperationType.builder().operationTypeId(4L).description("PAYMENT").build();
        Transaction tx = Transaction.builder().transactionId(1L).account(acc).operationType(op).amount(100.0).eventDate(LocalDateTime.now()).build();

        when(transactionService.createTransaction(anyLong(), anyLong(), anyDouble())).thenReturn(tx);

        TransactionRequest req = new TransactionRequest(1L, 4L, 100.0);

        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(1))
                .andExpect(jsonPath("$.amount").value(100.0));
    }

    @Test
    void createTransaction_invalidAccount_shouldReturnBadRequest() throws Exception {
        when(transactionService.createTransaction(anyLong(), anyLong(), anyDouble()))
                .thenThrow(new IllegalArgumentException("Invalid account ID"));

        TransactionRequest req = new TransactionRequest(99L, 1L, 50.0);

        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid account ID"));
    }

    @Test
    void createTransaction_invalidOperationType_shouldReturnBadRequest() throws Exception {
        when(transactionService.createTransaction(anyLong(), anyLong(), anyDouble()))
                .thenThrow(new IllegalArgumentException("Invalid operation type ID"));

        TransactionRequest req = new TransactionRequest(99L, 1L, 50.0);

        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid operation type ID"));
    }
}
