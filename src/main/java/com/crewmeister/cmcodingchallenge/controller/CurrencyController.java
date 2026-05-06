package com.crewmeister.cmcodingchallenge.controller;

import com.crewmeister.cmcodingchallenge.dto.ConversionResult;
import com.crewmeister.cmcodingchallenge.dto.ErrorResponse;
import com.crewmeister.cmcodingchallenge.entity.ExchangeRate;
import com.crewmeister.cmcodingchallenge.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST controller for handling currency exchange rate operations.
 * Provides endpoints to retrieve currencies, rates, and perform conversions.
 *
 * API Version: v1
 * Base Path: /api/v1
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Currency Exchange API v1", description = "Version 1 of the Currency Exchange API - Exchange rate operations and conversions")
public class CurrencyController extends BaseCurrencyController {

    /**
     * CurrencyController constructor.
     * @param exchangeRateService The exchange rate service
     */
    public CurrencyController(ExchangeRateService exchangeRateService) {
        super(exchangeRateService);
    }

    /**
     * Retrieves the list of all available currencies.
     * @return List of currency codes (e.g., ["USD", "EUR"])
     */
    @Operation(summary = "Get Available Currencies", description = "Retrieves the list of all available currencies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currencies retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "array", example = "[\"USD\", \"GBP\", \"JPY\", \"CHF\", \"CAD\", \"AUD\"]")))
    })
    @GetMapping("/currencies")
    public org.springframework.http.ResponseEntity<List<String>> getAvailableCurrencies() {
        return super.getAvailableCurrencies();
    }

    /**
     * Retrieves all exchange rates across all dates and currencies.
     * @return List of ExchangeRate objects
     */
    @Operation(
            summary = "Get All Exchange Rates",
            description = "Retrieves all exchange rates across all dates and currencies. Note: This may return a large dataset."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All rates retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExchangeRate.class, type = "array")))
    })
    @GetMapping("/rates")
    public org.springframework.http.ResponseEntity<List<ExchangeRate>> getAllRates() {
        return super.getAllRates();
    }

    /**
     * Retrieves exchange rates for a specific date across all currencies.
     * @param dateStr Date in YYYY-MM-DD format
     * @return List of ExchangeRate objects for the given date
     */
    @Operation(
            summary = "Get Exchange Rates for Date",
            description = "Retrieves exchange rates for a specific date across all currencies. Returns rates for all available currencies on the specified date."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rates retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExchangeRate.class, type = "array"))),
            @ApiResponse(responseCode = "400", description = "Invalid date format. Use YYYY-MM-DD format.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "No rates found for the specified date.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/rates/{date}")
    public org.springframework.http.ResponseEntity<List<ExchangeRate>> getCurrencyRatesForDate(
            @Parameter(description = "Date in YYYY-MM-DD format", example = "2024-05-01", required = true)
            @PathVariable("date") String dateStr) {
        return super.getCurrencyRatesForDate(dateStr);
    }

    /**
     * Converts a given amount from a foreign currency to EUR on a specific date.
     * @param from The currency to convert from
     * @param amount The amount to convert
     * @param date The date for the exchange rate (YYYY-MM-DD)
     * @return Converted amount in EUR
     */
    @Operation(
            summary = "Convert Currency to EUR",
            description = "Converts a given amount from a foreign currency to EUR on a specific date. Uses the official Bundesbank exchange rates."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currency converted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConversionResult.class),
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = "{\"fromCurrency\":\"USD\",\"fromAmount\":100.00,\"toCurrency\":\"EUR\",\"toAmount\":85.000000,\"conversionDate\":\"2024-05-01\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters (negative amount, invalid date format, future date, etc.)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Exchange rate not found for the specified currency and date",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/convert")
    public org.springframework.http.ResponseEntity<ConversionResult> convertCurrency(
            @Parameter(description = "Source currency code (ISO 4217)", example = "USD", required = true)
            @RequestParam String from,
            @Parameter(description = "Amount to convert (must be positive)", example = "100.00", required = true)
            @RequestParam BigDecimal amount,
            @Parameter(description = "Date for exchange rate in YYYY-MM-DD format (cannot be future date)", example = "2024-05-01", required = true)
            @RequestParam String date) {
        return super.convertCurrency(from, amount, date);
    }
}
