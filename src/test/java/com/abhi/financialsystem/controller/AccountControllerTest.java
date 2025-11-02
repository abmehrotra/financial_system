package com.abhi.financialsystem.controller;

import com.abhi.financialsystem.dto.AccountRequest;
import com.abhi.financialsystem.model.Account;
import com.abhi.financialsystem.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = AccountController.class)
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAccount_shouldReturnOk() throws Exception {
        Account acc = Account.builder().accountId(1L).documentNumber("12345678900").build();
        when(accountService.createAccount(anyString())).thenReturn(acc);

        AccountRequest req = new AccountRequest("12345678900");

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.documentNumber").value("12345678900"));
    }

    @Test
    void createAccount_whenAlreadyExists_shouldReturnBadRequest() throws Exception {
        when(accountService.createAccount(anyString()))
                .thenThrow(new IllegalArgumentException("Account with this document number already exists"));

        AccountRequest req = new AccountRequest("12345678900");

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Account with this document number already exists"));
    }

    @Test
    void getAccount_whenFound_shouldReturnAccountDetails() throws Exception {
        Account account = Account.builder()
                .accountId(99L)
                .documentNumber("12345678900")
                .build();

        when(accountService.getAccount(99L)).thenReturn(account);

        mockMvc.perform(get("/api/v1/accounts")
                        .param("accountId", "99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(99))
                .andExpect(jsonPath("$.documentNumber").value("12345678900"));
    }

    @Test
    void getAccount_whenNotFound_shouldReturnBadRequest() throws Exception {
        when(accountService.getAccount(99L))
                .thenThrow(new IllegalArgumentException("Account not found"));

        mockMvc.perform(get("/api/v1/accounts")
                        .param("accountId", "99"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Account not found"));
    }
}
