package com.crewmeister.cmcodingchallenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * Request object for currency conversion operations.
 * Used in bulk conversion requests.
 */
@Schema(description = "Currency conversion request")
public class ConversionRequest {

    @Schema(description = "Source currency code (ISO 4217)", example = "USD", required = true, minLength = 3, maxLength = 3)
    private String from;

    @Schema(description = "Amount to convert (must be positive)", example = "100.00", required = true, minimum = "0.01")
    private BigDecimal amount;

    @Schema(description = "Date for exchange rate in YYYY-MM-DD format", example = "2024-05-01", required = true)
    private String date;

    public ConversionRequest() {}

    public ConversionRequest(String from, BigDecimal amount, String date) {
        this.from = from;
        this.amount = amount;
        this.date = date;
    }

    // Getters and setters
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
