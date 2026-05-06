package com.crewmeister.cmcodingchallenge.controller;

import com.crewmeister.cmcodingchallenge.dto.ConversionResult;
import com.crewmeister.cmcodingchallenge.entity.ExchangeRate;
import com.crewmeister.cmcodingchallenge.service.ExchangeRateService;
import com.crewmeister.cmcodingchallenge.exception.ExchangeRateNotFoundException;
import com.crewmeister.cmcodingchallenge.exception.InvalidInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Base controller for currency exchange operations.
 * Provides common functionality that can be shared across API versions.
 *
 * This abstract class defines the core business logic and validation,
 * allowing version-specific controllers to focus on API contract changes.
 */
public abstract class BaseCurrencyController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final ExchangeRateService exchangeRateService;

    @Autowired
    public BaseCurrencyController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    /**
     * Retrieves the list of all available currencies.
     * @return List of currency codes (e.g., ["USD", "EUR"])
     */
    protected ResponseEntity<List<String>> getAvailableCurrencies() {
        logger.info("Fetching available currencies");
        List<String> currencyList = exchangeRateService.getAvailableCurrencies();
        logger.debug("Retrieved {} currencies", currencyList.size());
        return ResponseEntity.ok(currencyList);
    }

    /**
     * Retrieves all exchange rates across all dates and currencies.
     * @return List of ExchangeRate objects
     */
    protected ResponseEntity<List<ExchangeRate>> getAllRates() {
        logger.info("Fetching all exchange rates");
        List<ExchangeRate> allRates = exchangeRateService.getAllExchangeRates();
        logger.debug("Retrieved {} exchange rate records", allRates.size());
        return ResponseEntity.ok(allRates);
    }

    /**
     * Retrieves exchange rates for a specific date across all currencies.
     * @param dateStr Date in YYYY-MM-DD format
     * @return List of ExchangeRate objects for the given date
     * @throws InvalidInputException if date format is invalid
     * @throws ExchangeRateNotFoundException if no rates found for the date
     */
    protected ResponseEntity<List<ExchangeRate>> getCurrencyRatesForDate(String dateStr) {
        try {
            logger.info("Fetching exchange rates for date: {}", dateStr);
            LocalDate requestedDate = LocalDate.parse(dateStr);
            List<ExchangeRate> ratesForDate = exchangeRateService.getRatesForDate(requestedDate);

            if (ratesForDate.isEmpty()) {
                logger.warn("No exchange rates found for date: {}", dateStr);
                throw new ExchangeRateNotFoundException("No rates available for date: " + dateStr);
            }

            logger.debug("Retrieved {} rates for date {}", ratesForDate.size(), dateStr);
            return ResponseEntity.ok(ratesForDate);
        } catch (DateTimeParseException e) {
            logger.error("Invalid date format provided: {}", dateStr);
            throw new InvalidInputException(
                    "Invalid date format. Please use YYYY-MM-DD format. Received: " + dateStr,
                    e
            );
        }
    }

    /**
     * Converts a given amount from a foreign currency to EUR on a specific date.
     * @param from The currency to convert from
     * @param amount The amount to convert
     * @param date The date for the exchange rate (YYYY-MM-DD)
     * @return Converted amount in EUR
     * @throws InvalidInputException if input validation fails
     * @throws ExchangeRateNotFoundException if rate not available for date
     */
    protected ResponseEntity<ConversionResult> convertCurrency(String from, BigDecimal amount, String date) {
        logger.info("Converting {} {} to EUR on date {}", amount, from, date);

        // Validate inputs
        if (from == null || from.trim().isEmpty()) {
            logger.error("Currency parameter 'from' is missing or empty");
            throw new InvalidInputException("Parameter 'from' is required and cannot be empty");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Invalid amount: {}", amount);
            throw new InvalidInputException("Amount must be a positive number. Received: " + amount);
        }

        if (date == null || date.trim().isEmpty()) {
            logger.error("Date parameter is missing or empty");
            throw new InvalidInputException("Parameter 'date' is required and cannot be empty");
        }

        try {
            LocalDate conversionDate = LocalDate.parse(date);

            // Validate date is not in the future
            if (conversionDate.isAfter(LocalDate.now())) {
                logger.warn("Conversion requested for future date: {}", date);
                throw new InvalidInputException("Exchange rates are not available for future dates. Date: " + date);
            }

            BigDecimal convertedAmount = exchangeRateService.convertToEur(from, amount, conversionDate);

            logger.info("Successfully converted {} {} to {} EUR", amount, from, convertedAmount);
            ConversionResult result = new ConversionResult(
                    from,
                    amount,
                    "EUR",
                    convertedAmount,
                    conversionDate
            );
            return ResponseEntity.ok(result);
        } catch (DateTimeParseException e) {
            logger.error("Invalid date format provided: {}", date);
            throw new InvalidInputException(
                    "Invalid date format. Please use YYYY-MM-DD format. Received: " + date,
                    e
            );
        }
    }
}
