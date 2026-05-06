package com.crewmeister.cmcodingchallenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Response DTO for currency conversion operations.
 * Provides complete information about the conversion result.
 */
@Schema(description = "Currency conversion result containing all conversion details")
public class ConversionResult {
    @Schema(description = "Source currency code", example = "USD", minLength = 3, maxLength = 3)
    private String fromCurrency;

    @Schema(description = "Amount to convert", example = "100.00", minimum = "0.01")
    private BigDecimal fromAmount;

    @Schema(description = "Target currency code (always EUR)", example = "EUR", minLength = 3, maxLength = 3)
    private String toCurrency;

    @Schema(description = "Converted amount in target currency", example = "85.000000")
    private BigDecimal toAmount;

    @Schema(description = "Date used for the exchange rate", example = "2024-05-01", format = "date")
    private LocalDate conversionDate;

    public ConversionResult(String fromCurrency, BigDecimal fromAmount, 
                           String toCurrency, BigDecimal toAmount, LocalDate conversionDate) {
        this.fromCurrency = fromCurrency;
        this.fromAmount = fromAmount;
        this.toCurrency = toCurrency;
        this.toAmount = toAmount;
        this.conversionDate = conversionDate;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public BigDecimal getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(BigDecimal fromAmount) {
        this.fromAmount = fromAmount;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public BigDecimal getToAmount() {
        return toAmount;
    }

    public void setToAmount(BigDecimal toAmount) {
        this.toAmount = toAmount;
    }

    public LocalDate getConversionDate() {
        return conversionDate;
    }

    public void setConversionDate(LocalDate conversionDate) {
        this.conversionDate = conversionDate;
    }
}
