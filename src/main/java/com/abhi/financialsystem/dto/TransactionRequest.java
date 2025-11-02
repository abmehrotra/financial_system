package com.abhi.financialsystem.dto;

import jakarta.validation.constraints.NotNull;

public record TransactionRequest(
        @NotNull(message = "Account Id is required") Long accountId,
        @NotNull(message = "Operation type is required") Long operationTypeId,
        @NotNull(message = "Amount is required") Double amount
) {
}
