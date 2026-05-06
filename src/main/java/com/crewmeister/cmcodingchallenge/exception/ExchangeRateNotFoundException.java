package com.crewmeister.cmcodingchallenge.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when an exchange rate is not found for a given currency and date.
 */
public class ExchangeRateNotFoundException extends ApplicationException {
    public ExchangeRateNotFoundException(String currency, String date) {
        super(
            String.format("Exchange rate not found for currency '%s' on date '%s'", currency, date),
            HttpStatus.NOT_FOUND.value(),
            "EXCHANGE_RATE_NOT_FOUND"
        );
    }

    public ExchangeRateNotFoundException(String currency) {
        super(
            String.format("No exchange rate data available for currency '%s'", currency),
            HttpStatus.NOT_FOUND.value(),
            "CURRENCY_NOT_FOUND"
        );
    }
}

