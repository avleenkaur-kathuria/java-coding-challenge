package com.crewmeister.cmcodingchallenge.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * JPA entity representing an exchange rate for a specific currency and date.
 * Stores the rate as EUR per unit of the foreign currency.
 */
@Schema(description = "Exchange rate information for a specific currency and date")
@Entity
@Table(name = "exchange_rates", indexes = {
    @Index(name = "idx_currency_date", columnList = "currency, date")
})
public class ExchangeRate {

    @Schema(description = "Unique identifier for the exchange rate record", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Currency code (ISO 4217)", example = "USD", minLength = 3, maxLength = 3)
    @NotNull
    @Column(nullable = false, length = 3)
    private String currency;

    @Schema(description = "Date of the exchange rate", example = "2024-05-01", format = "date")
    @NotNull
    @Column(nullable = false)
    private LocalDate date;

    @Schema(description = "Exchange rate (EUR per unit of foreign currency)", example = "0.850000", minimum = "0.000001")
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal rate;

    // Default constructor for JPA
    public ExchangeRate() {}

    /**
     * Constructor for creating an exchange rate entry.
     * @param currency The currency code (e.g., "USD")
     * @param date The date of the rate
     * @param rate The exchange rate (EUR per unit of currency)
     */
    public ExchangeRate(String currency, LocalDate date, BigDecimal rate) {
        this.currency = currency;
        this.date = date;
        this.rate = rate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ExchangeRate that = (ExchangeRate) obj;
        return Objects.equals(currency, that.currency) &&
               Objects.equals(date, that.date) &&
               Objects.equals(rate, that.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, date, rate);
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "id=" + id +
                ", currency='" + currency + '\'' +
                ", date=" + date +
                ", rate=" + rate +
                '}';
    }
}
