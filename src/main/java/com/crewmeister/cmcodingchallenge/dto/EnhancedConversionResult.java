package com.crewmeister.cmcodingchallenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Enhanced conversion result for API v2.
 * Includes additional metadata and rate information.
 */
@Schema(description = "Enhanced currency conversion result with metadata")
public class EnhancedConversionResult {

    @Schema(description = "Source currency code", example = "USD", minLength = 3, maxLength = 3)
    private String fromCurrency;

    @Schema(description = "Amount to convert", example = "100.00", minimum = "0.01")
    private BigDecimal fromAmount;

    @Schema(description = "Target currency code", example = "EUR", minLength = 3, maxLength = 3)
    private String toCurrency;

    @Schema(description = "Converted amount in target currency", example = "85.000000")
    private BigDecimal toAmount;

    @Schema(description = "Date used for the exchange rate", example = "2024-05-01", format = "date")
    private LocalDate conversionDate;

    @Schema(description = "Exchange rate used for conversion", example = "0.850000")
    private BigDecimal exchangeRate;

    @Schema(description = "Data source for the exchange rate", example = "Bundesbank")
    private String dataSource;

    @Schema(description = "Timestamp when the conversion was performed", example = "2024-05-01T10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "API version used", example = "v2")
    private String apiVersion;

    public EnhancedConversionResult(String fromCurrency, BigDecimal fromAmount, String toCurrency,
                                  BigDecimal toAmount, LocalDate conversionDate, BigDecimal exchangeRate,
                                  String dataSource, LocalDateTime timestamp, String apiVersion) {
        this.fromCurrency = fromCurrency;
        this.fromAmount = fromAmount;
        this.toCurrency = toCurrency;
        this.toAmount = toAmount;
        this.conversionDate = conversionDate;
        this.exchangeRate = exchangeRate;
        this.dataSource = dataSource;
        this.timestamp = timestamp;
        this.apiVersion = apiVersion;
    }

    // Getters and setters
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

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }
}
