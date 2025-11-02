package com.abhi.financialsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AccountRequest(
        @NotBlank(message = "Document number is required")
        @Pattern(regexp = "\\d+", message = "Document number must be numeric")
        String documentNumber) {
}
