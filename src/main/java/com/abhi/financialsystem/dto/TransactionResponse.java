package com.abhi.financialsystem.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TransactionResponse(
        Long transactionId,
        Long accountId,
        Long operationTypeId,
        Double amount,
        LocalDateTime eventDate
) {
}
