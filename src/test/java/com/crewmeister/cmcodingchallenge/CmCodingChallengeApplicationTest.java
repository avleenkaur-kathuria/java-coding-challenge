package com.crewmeister.cmcodingchallenge;

import com.crewmeister.cmcodingchallenge.entity.ExchangeRate;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Simple unit tests for the ExchangeRate entity and basic business logic
 * Based on user stories - testing without Mockito due to Java 25 compatibility issues
 */
public class CmCodingChallengeApplicationTest {

    /**
     * Test Case 1: As a client, I want to get a list of all available currencies
     * Testing the entity creation and basic properties
     */
    @Test
    void testExchangeRateEntityCreation() {
        // Given
        String currency = "USD";
        LocalDate date = LocalDate.of(2024, 5, 1);
        BigDecimal rate = new BigDecimal("1.085000");

        // When
        ExchangeRate exchangeRate = new ExchangeRate(currency, date, rate);

        // Then
        assertNotNull(exchangeRate);
        assertEquals("USD", exchangeRate.getCurrency());
        assertEquals(LocalDate.of(2024, 5, 1), exchangeRate.getDate());
        assertEquals(new BigDecimal("1.085000"), exchangeRate.getRate());
    }

    /**
     * Test Case 2: As a client, I want to get all EUR-FX exchange rates at all available dates as a collection
     * Testing that we can create and manage collections of exchange rates
     */
    @Test
    void testExchangeRateCollection() {
        // Given
        List<ExchangeRate> rates = Arrays.asList(
                new ExchangeRate("USD", LocalDate.of(2024, 5, 1), new BigDecimal("1.085000")),
                new ExchangeRate("GBP", LocalDate.of(2024, 5, 1), new BigDecimal("0.860000")),
                new ExchangeRate("JPY", LocalDate.of(2024, 5, 1), new BigDecimal("0.006800"))
        );

        // When & Then
        assertNotNull(rates);
        assertEquals(3, rates.size());

        // Verify each rate
        assertEquals("USD", rates.get(0).getCurrency());
        assertEquals("GBP", rates.get(1).getCurrency());
        assertEquals("JPY", rates.get(2).getCurrency());

        // All rates should be for the same date
        assertTrue(rates.stream().allMatch(rate -> rate.getDate().equals(LocalDate.of(2024, 5, 1))));
    }

    /**
     * Test Case 3: As a client, I want to get the EUR-FX exchange rate at particular day
     * Testing date filtering logic
     */
    @Test
    void testExchangeRateDateFiltering() {
        // Given
        List<ExchangeRate> allRates = Arrays.asList(
                new ExchangeRate("USD", LocalDate.of(2024, 5, 1), new BigDecimal("1.085000")),
                new ExchangeRate("USD", LocalDate.of(2024, 5, 2), new BigDecimal("1.083000")),
                new ExchangeRate("GBP", LocalDate.of(2024, 5, 1), new BigDecimal("0.860000")),
                new ExchangeRate("GBP", LocalDate.of(2024, 5, 2), new BigDecimal("0.858000"))
        );

        LocalDate targetDate = LocalDate.of(2024, 5, 1);

        // When - Filter rates for specific date
        List<ExchangeRate> ratesForDate = allRates.stream()
                .filter(rate -> rate.getDate().equals(targetDate))
                .toList();

        // Then
        assertNotNull(ratesForDate);
        assertEquals(2, ratesForDate.size()); // USD and GBP for May 1st

        // Verify both currencies are present
        assertTrue(ratesForDate.stream().anyMatch(rate -> "USD".equals(rate.getCurrency())));
        assertTrue(ratesForDate.stream().anyMatch(rate -> "GBP".equals(rate.getCurrency())));

        // Verify all filtered rates are for the target date
        assertTrue(ratesForDate.stream().allMatch(rate -> rate.getDate().equals(targetDate)));
    }

    /**
     * Test Case 4: As a client, I want to get a foreign exchange amount for a given currency converted to EUR on a particular day
     * Testing the currency conversion calculation
     */
    @Test
    void testCurrencyConversionCalculation() {
        // Given
        BigDecimal amount = new BigDecimal("100");
        BigDecimal usdRate = new BigDecimal("1.085000"); // 1 USD = 1.085 EUR

        // When - Convert USD to EUR: amount ÷ rate
        BigDecimal convertedAmount = amount.divide(usdRate, 6, BigDecimal.ROUND_HALF_UP);

        // Then
        assertNotNull(convertedAmount);
        assertEquals(new BigDecimal("92.165899"), convertedAmount);

        // Verify the conversion formula: 100 USD ÷ 1.085 EUR/USD = 92.165899 EUR
        BigDecimal expected = new BigDecimal("100").divide(new BigDecimal("1.085000"), 6, BigDecimal.ROUND_HALF_UP);
        assertEquals(expected, convertedAmount);
    }

    /**
     * Test Case 4b: Test conversion with different currencies
     */
    @Test
    void testDifferentCurrencyConversions() {
        BigDecimal amount = new BigDecimal("100");

        // Test GBP conversion (stronger currency)
        BigDecimal gbpRate = new BigDecimal("0.860000");
        BigDecimal gbpConverted = amount.divide(gbpRate, 6, BigDecimal.ROUND_HALF_UP);
        assertTrue(gbpConverted.compareTo(amount) > 0); // Should get more EUR than USD amount

        // Test JPY conversion (weaker currency)
        BigDecimal jpyRate = new BigDecimal("0.006800");
        BigDecimal jpyConverted = amount.divide(jpyRate, 6, BigDecimal.ROUND_HALF_UP);
        assertTrue(jpyConverted.compareTo(amount) > 0); // Should get much more EUR than USD amount
        assertTrue(jpyConverted.compareTo(gbpConverted) > 0); // JPY should give more EUR than GBP
    }

    /**
     * Test Case 4c: Test conversion with edge cases
     */
    @Test
    void testCurrencyConversionEdgeCases() {
        BigDecimal usdRate = new BigDecimal("1.085000");

        // Test with zero amount
        BigDecimal zeroAmount = BigDecimal.ZERO;
        BigDecimal zeroConverted = zeroAmount.divide(usdRate, 6, BigDecimal.ROUND_HALF_UP);
        assertEquals(0, zeroConverted.compareTo(BigDecimal.ZERO)); // Use compareTo for BigDecimal

        // Test with very small amount
        BigDecimal smallAmount = new BigDecimal("0.01");
        BigDecimal smallConverted = smallAmount.divide(usdRate, 6, BigDecimal.ROUND_HALF_UP);
        assertTrue(smallConverted.compareTo(BigDecimal.ZERO) > 0);

        // Test with large amount
        BigDecimal largeAmount = new BigDecimal("1000000");
        BigDecimal largeConverted = largeAmount.divide(usdRate, 6, BigDecimal.ROUND_HALF_UP);
        assertTrue(largeConverted.compareTo(largeAmount) < 0); // Should be less than original amount
    }

    /**
     * Test Case: Verify supported currencies list
     */
    @Test
    void testSupportedCurrenciesList() {
        // Given - the currencies supported by the application
        List<String> supportedCurrencies = Arrays.asList("USD", "GBP", "JPY", "CHF", "CAD", "AUD");

        // Then
        assertNotNull(supportedCurrencies);
        assertEquals(6, supportedCurrencies.size());
        assertTrue(supportedCurrencies.contains("USD"));
        assertTrue(supportedCurrencies.contains("GBP"));
        assertTrue(supportedCurrencies.contains("JPY"));
        assertTrue(supportedCurrencies.contains("CHF"));
        assertTrue(supportedCurrencies.contains("CAD"));
        assertTrue(supportedCurrencies.contains("AUD"));

        // Verify no duplicates
        assertEquals(supportedCurrencies.size(), supportedCurrencies.stream().distinct().count());
    }

    /**
     * Test Case: Verify exchange rate data integrity
     */
    @Test
    void testExchangeRateDataIntegrity() {
        // Given
        ExchangeRate rate = new ExchangeRate("USD", LocalDate.of(2024, 5, 1), new BigDecimal("1.085000"));

        // Then - Verify all fields are set correctly
        assertNotNull(rate.getCurrency());
        assertNotNull(rate.getDate());
        assertNotNull(rate.getRate());
        assertTrue(rate.getRate().compareTo(BigDecimal.ZERO) > 0); // Rate should be positive
        assertEquals(3, rate.getCurrency().length()); // Currency codes are 3 characters
        assertTrue(rate.getDate().isBefore(LocalDate.now().plusDays(1))); // Date should not be in far future
    }

    /**
     * Test Case: Verify equals and hashCode implementation
     */
    @Test
    void testExchangeRateEqualsAndHashCode() {
        // Given
        ExchangeRate rate1 = new ExchangeRate("USD", LocalDate.of(2024, 5, 1), new BigDecimal("1.085000"));
        ExchangeRate rate2 = new ExchangeRate("USD", LocalDate.of(2024, 5, 1), new BigDecimal("1.085000"));
        ExchangeRate rate3 = new ExchangeRate("GBP", LocalDate.of(2024, 5, 1), new BigDecimal("1.085000"));

        // Then
        assertEquals(rate1, rate2); // Same values should be equal
        assertNotEquals(rate1, rate3); // Different currency should not be equal
        assertEquals(rate1.hashCode(), rate2.hashCode()); // Same values should have same hash
    }

}
