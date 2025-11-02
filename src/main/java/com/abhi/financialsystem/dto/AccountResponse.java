package com.abhi.financialsystem.dto;

import lombok.Builder;

@Builder
public record AccountResponse(Long accountId, String documentNumber) {
}
