package com.crewmeister.cmcodingchallenge.controller;

import com.crewmeister.cmcodingchallenge.dto.*;
import com.crewmeister.cmcodingchallenge.entity.ExchangeRate;
import com.crewmeister.cmcodingchallenge.exception.InvalidInputException;
import com.crewmeister.cmcodingchallenge.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Version 2 of the Currency Exchange API.
 * Demonstrates API versioning with enhanced features.
 *
 * API Version: v2
 * Base Path: /api/v2
 *
 * New features in v2:
 * - Enhanced response format with metadata
 * - Support for multiple target currencies
 * - Bulk conversion operations
 * - Rate comparison endpoints
 */
@RestController
@RequestMapping("/api/v2")
@Tag(name = "Currency Exchange API v2", description = "Version 2 of the Currency Exchange API - Enhanced features with metadata and bulk operations")
public class CurrencyControllerV2 extends BaseCurrencyController {

    /**
     * CurrencyControllerV2 constructor.
     * @param exchangeRateService The exchange rate service
     */
    public CurrencyControllerV2(ExchangeRateService exchangeRateService) {
        super(exchangeRateService);
    }

    /**
     * Retrieves the list of all available currencies with metadata.
     * @return Enhanced currency list with metadata
     */
    @Operation(
            summary = "Get Available Currencies (Enhanced)",
            description = "Retrieves the list of all available currencies with additional metadata including currency names and regions"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currencies retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CurrencyMetadataResponse.class)))
    })
    @GetMapping("/currencies")
    public org.springframework.http.ResponseEntity<CurrencyMetadataResponse> getAvailableCurrenciesV2() {
        List<String> currencies = exchangeRateService.getAvailableCurrencies();

        // Create enhanced response with metadata
        Map<String, CurrencyMetadataResponse.CurrencyInfo> currencyInfo = Map.of(
                "USD", new CurrencyMetadataResponse.CurrencyInfo("US Dollar", "United States", "America"),
                "GBP", new CurrencyMetadataResponse.CurrencyInfo("British Pound", "United Kingdom", "Europe"),
                "JPY", new CurrencyMetadataResponse.CurrencyInfo("Japanese Yen", "Japan", "Asia"),
                "CHF", new CurrencyMetadataResponse.CurrencyInfo("Swiss Franc", "Switzerland", "Europe"),
                "CAD", new CurrencyMetadataResponse.CurrencyInfo("Canadian Dollar", "Canada", "America"),
                "AUD", new CurrencyMetadataResponse.CurrencyInfo("Australian Dollar", "Australia", "Oceania")
        );

        CurrencyMetadataResponse response = new CurrencyMetadataResponse(
                currencies,
                currencyInfo,
                currencies.size(),
                "v2"
        );

        return org.springframework.http.ResponseEntity.ok(response);
    }

    /**
     * Retrieves all exchange rates with pagination support.
     * @param page Page number (0-based)
     * @param size Page size
     * @return Paginated list of ExchangeRate objects
     */
    @Operation(
            summary = "Get All Exchange Rates (Paginated)",
            description = "Retrieves all exchange rates with pagination support for better performance"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rates retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PaginatedExchangeRateResponse.class)))
    })
    @GetMapping("/rates")
    public org.springframework.http.ResponseEntity<PaginatedExchangeRateResponse> getAllRates(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "50")
            @RequestParam(defaultValue = "50") int size) {

        List<ExchangeRate> allRates = exchangeRateService.getAllExchangeRates();

        // Simple pagination implementation
        int totalElements = allRates.size();
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalElements);

        List<ExchangeRate> pageContent = startIndex < totalElements ?
                allRates.subList(startIndex, endIndex) : List.of();

        PaginatedExchangeRateResponse response = new PaginatedExchangeRateResponse(
                pageContent,
                page,
                size,
                totalElements,
                (int) Math.ceil((double) totalElements / size),
                page < Math.ceil((double) totalElements / size) - 1,
                page > 0
        );

        return org.springframework.http.ResponseEntity.ok(response);
    }

    /**
     * Converts currency with enhanced response format.
     * @param from The currency to convert from
     * @param to The target currency (defaults to EUR)
     * @param amount The amount to convert
     * @param date The date for the exchange rate
     * @return Enhanced conversion result
     */
    @Operation(
            summary = "Convert Currency (Enhanced)",
            description = "Converts currency with enhanced response format including conversion metadata and rate information"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currency converted successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EnhancedConversionResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Exchange rate not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/convert")
    public org.springframework.http.ResponseEntity<EnhancedConversionResult> convertCurrency(
            @Parameter(description = "Source currency code (ISO 4217)", example = "USD", required = true)
            @RequestParam String from,
            @Parameter(description = "Target currency code (ISO 4217, defaults to EUR)", example = "EUR")
            @RequestParam(defaultValue = "EUR") String to,
            @Parameter(description = "Amount to convert (must be positive)", example = "100.00", required = true)
            @RequestParam BigDecimal amount,
            @Parameter(description = "Date for exchange rate in YYYY-MM-DD format", example = "2024-05-01", required = true)
            @RequestParam String date) {

        // For v2, we'll focus on EUR conversions but structure allows for multi-currency
        if (!"EUR".equals(to)) {
            throw new InvalidInputException("Version 2 currently only supports EUR as target currency. Use 'EUR' for the 'to' parameter.");
        }

        // Use the base conversion logic
        var baseResponse = super.convertCurrency(from, amount, date);
        var conversionResult = baseResponse.getBody();

        if (conversionResult == null) {
            return org.springframework.http.ResponseEntity.notFound().build();
        }

        // Create enhanced response
        EnhancedConversionResult enhancedResult = new EnhancedConversionResult(
                conversionResult.getFromCurrency(),
                conversionResult.getFromAmount(),
                conversionResult.getToCurrency(),
                conversionResult.getToAmount(),
                conversionResult.getConversionDate(),
                conversionResult.getToAmount().divide(conversionResult.getFromAmount(), 6, java.math.RoundingMode.HALF_UP),
                "Bundesbank",
                java.time.LocalDateTime.now(),
                "v2"
        );

        return org.springframework.http.ResponseEntity.ok(enhancedResult);
    }

    /**
     * Bulk currency conversion endpoint.
     * @param conversions List of conversion requests
     * @return List of conversion results
     */
    @Operation(
            summary = "Bulk Currency Conversion",
            description = "Converts multiple currency amounts in a single request for better performance"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bulk conversion completed successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BulkConversionResult.class)))
    })
    @PostMapping("/convert/bulk")
    public org.springframework.http.ResponseEntity<BulkConversionResult> bulkConvert(
            @Parameter(description = "List of conversion requests")
            @RequestBody List<ConversionRequest> conversions) {

        List<EnhancedConversionResult> results = conversions.stream()
                .map(req -> {
                    try {
                        var response = super.convertCurrency(req.getFrom(), req.getAmount(), req.getDate());
                        var result = response.getBody();
                        if (result != null) {
                            return new EnhancedConversionResult(
                                    result.getFromCurrency(),
                                    result.getFromAmount(),
                                    result.getToCurrency(),
                                    result.getToAmount(),
                                    result.getConversionDate(),
                                    result.getToAmount().divide(result.getFromAmount(), 6, java.math.RoundingMode.HALF_UP),
                                    "Bundesbank",
                                    java.time.LocalDateTime.now(),
                                    "v2"
                            );
                        } else {
                            // Handle null result
                            return new EnhancedConversionResult(
                                    req.getFrom(),
                                    req.getAmount(),
                                    "EUR",
                                    BigDecimal.ZERO,
                                    java.time.LocalDate.now(), // Use current date as fallback
                                    BigDecimal.ZERO,
                                    "Bundesbank",
                                    java.time.LocalDateTime.now(),
                                    "v2"
                            );
                        }
                    } catch (Exception e) {
                        // Return error result for failed conversions
                        java.time.LocalDate conversionDate;
                        try {
                            conversionDate = java.time.LocalDate.parse(req.getDate());
                        } catch (Exception dateEx) {
                            conversionDate = java.time.LocalDate.now(); // Fallback for invalid date
                        }
                        return new EnhancedConversionResult(
                                req.getFrom(),
                                req.getAmount(),
                                "EUR",
                                BigDecimal.ZERO,
                                conversionDate,
                                BigDecimal.ZERO,
                                "Bundesbank",
                                java.time.LocalDateTime.now(),
                                "v2"
                        );
                    }
                })
                .toList();

        BulkConversionResult response = new BulkConversionResult(
                results,
                results.size(),
                results.stream().filter(r -> r.getToAmount().compareTo(BigDecimal.ZERO) > 0).count(),
                results.stream().filter(r -> r.getToAmount().compareTo(BigDecimal.ZERO) == 0).count(),
                java.time.LocalDateTime.now(),
                "v2"
        );

        return org.springframework.http.ResponseEntity.ok(response);
    }
}
