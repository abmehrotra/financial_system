package com.abhi.financialsystem.dto;

import jakarta.validation.constraints.NotBlank;

public record AccountRequest(
        @NotBlank(message = "Document number is required")
        String documentNumber) {
}
