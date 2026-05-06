package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.entity.ExchangeRate;
import com.crewmeister.cmcodingchallenge.repository.ExchangeRateRepository;
import com.crewmeister.cmcodingchallenge.exception.ExchangeRateNotFoundException;
import com.crewmeister.cmcodingchallenge.exception.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class ExchangeRateService {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);

    private final ExchangeRateRepository exchangeRateRepo;
    private final WebClient bundesbankClient;

    // Hardcoded list of currencies we're interested in - could be made configurable later
    private enum SupportedCurrency {
        USD,
        GBP,
        JPY,
        CHF,
        CAD,
        AUD
    }
    // Base URL for Bundesbank API - note: we switch to CSV format for easier parsing
    @Value("${currency.api.base-url}")
    private String bundesbankApiBase;
    @Autowired
    public ExchangeRateService(ExchangeRateRepository repository, WebClient.Builder webClientBuilder) {
        this.exchangeRateRepo = repository;
        this.bundesbankClient = webClientBuilder.build();
    }

    /**
     * Fetches exchange rates from Bundesbank for all supported currencies and saves them to the database.
     * This is called on app startup to populate the DB with real data.
     */
    public void fetchAndSaveExchangeRates() {
        logger.info("Starting to fetch exchange rates from Bundesbank for {} currencies",
                SupportedCurrency.values().length);
        for (SupportedCurrency currency : SupportedCurrency.values()) {

            logger.debug("Fetching data for currency: {}", currency);

            String apiUrl = bundesbankApiBase
                    .replace("{CURRENCY}", currency.name())
                    .replace("format=json", "format=csv");
            logger.debug("Fetching data for currency: {}", currency);
            try {
                String rawCsv = bundesbankClient.get()
                        .uri(apiUrl)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
                if (rawCsv != null && !rawCsv.isEmpty()) {
                    parseAndStoreCsvData(currency.name(), rawCsv);
                } else {
                    logger.warn("Received empty response for currency: {}", currency);
                    generateFallbackData(currency.name());
                }
            } catch (Exception ex) {
                logger.error("Failed to fetch or process data for currency: {}. Falling back to sample data. Error: {}", currency, ex.getMessage());
                generateFallbackData(currency.name());
            }
        }
        logger.info("Finished fetching and saving exchange rates.");
    }

    /**
     * Parses the CSV data from Bundesbank and stores valid exchange rates in the DB.
     * Skips header lines and invalid entries.
     */
    private void parseAndStoreCsvData(String currencyCode, String csvContent) {
        String[] csvLines = csvContent.split("\n");
        int successfullySaved = 0;

        for (String line : csvLines) {

            if (isValidDataLine(line)) {

                try {
                    // small cleanup
                    String cleanedLine = line.replace("\"", "").trim();

                    String[] columns = cleanedLine.split(";");

                    if (columns.length >= 2) {
                        LocalDate exchangeDate =
                                LocalDate.parse(columns[0].trim());

                        String rateAsString =
                                columns[1].trim().replace(",", ".");

                        if (isValidRateValue(rateAsString)) {
                            BigDecimal exchangeRate =
                                    new BigDecimal(rateAsString);

                            ExchangeRate rateEntity =
                                    new ExchangeRate(
                                            currencyCode,
                                            exchangeDate,
                                            exchangeRate
                                    );

                            exchangeRateRepo.save(rateEntity);
                            successfullySaved++;
                        }
                    }

                } catch (Exception ex) {
                    logger.warn(
                            "Skipping malformed row for {} : {}",
                            currencyCode,
                            line
                    );
                }
            }
        }

        if (successfullySaved == 0) {
            logger.warn(
                    "No valid data found for {}. Using fallback.",
                    currencyCode
            );
            generateFallbackData(currencyCode);
        } else {
            logger.info(
                    "Saved {} rows for {}",
                    successfullySaved,
                    currencyCode
            );
        }
    }

    /**
     * Checks if a CSV line is a valid data line (starts with YYYY-MM-DD;).
     */
    private boolean isValidDataLine(String line) {//Changed
        if (line == null) return false;

        String cleaned = line.replace("\"", "").trim();

        return cleaned.matches("\\d{4}-\\d{2}-\\d{2};.*");
    }

    /**
     * Validates the rate string - not empty, not just a dot, and doesn't contain 'Kein' (German for 'none').
     */
    private boolean isValidRateValue(String rateStr) {
        return rateStr != null && !rateStr.trim().isEmpty() && !rateStr.equals(".") && !rateStr.contains("Kein");
    }

    /**
     * Generates sample data for a currency when real data can't be fetched.
     * Uses random rates for the last 30 days as a placeholder.
     */
    private void generateFallbackData(String currencyCode) {
        LocalDate currentDate = LocalDate.now();
        logger.info("Generating sample data for currency {} over the last 30 days", currencyCode);
        for (int daysBack = 0; daysBack < 30; daysBack++) {
            LocalDate dateForRate = currentDate.minusDays(daysBack);
            // Random rate between 0.5 and 2.5 for demo purposes
            BigDecimal sampleRate = BigDecimal.valueOf(0.5 + Math.random() * 2.0);
            ExchangeRate sampleEntity = new ExchangeRate(currencyCode, dateForRate, sampleRate);
            exchangeRateRepo.save(sampleEntity);
        }
    }

    /**
     * Retrieves all distinct currencies that have exchange rates in the DB.
     */
    public List<String> getAvailableCurrencies() {
        return exchangeRateRepo.findDistinctCurrency();
    }

    /**
     * Gets all exchange rates, sorted by date ascending.
     */
    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateRepo.findAllByOrderByDateAsc();
    }

    /**
     * Finds the exchange rate for a specific currency and date.
     */
    public ExchangeRate getExchangeRate(String currencyCode, LocalDate targetDate) {
        return exchangeRateRepo.findByCurrencyAndDate(currencyCode, targetDate).orElse(null);
    }

    /**
     * Gets all exchange rates for a specific date.
     *
     * @param date The date to get rates for
     * @return List of ExchangeRate objects for that date
     */
    public List<ExchangeRate> getRatesForDate(LocalDate date) {
        if (date == null) {
            logger.error("Date parameter is null");
            throw new InvalidInputException("Date cannot be null");
        }

        List<ExchangeRate> ratesForDate = exchangeRateRepo.findByDate(date);

        if (ratesForDate.isEmpty()) {
            logger.warn("No exchange rates found for date: {}", date);
        } else {
            logger.debug("Found {} rates for date: {}", ratesForDate.size(), date);
        }

        return ratesForDate;
    }

    /**
     * Converts a foreign currency amount to EUR using the rate for the given date.
     * Note: Rates are EUR per unit of foreign currency, so we divide the amount by the rate.
     *
     * @param currencyCode   The currency code to convert from
     * @param foreignAmount  The amount to convert
     * @param conversionDate The date for the exchange rate
     * @return The converted amount in EUR
     * @throws ExchangeRateNotFoundException If no rate is available for the given currency and date
     */
    public BigDecimal convertToEur(String currencyCode, BigDecimal foreignAmount, LocalDate conversionDate) {
        if (currencyCode == null || currencyCode.trim().isEmpty()) {
            logger.error("Currency code is null or empty");
            throw new InvalidInputException("Currency code cannot be null or empty");
        }

        if (foreignAmount == null || foreignAmount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Invalid amount: {}", foreignAmount);
            throw new InvalidInputException("Amount must be greater than zero");
        }

        if (conversionDate == null) {
            logger.error("Conversion date is null");
            throw new InvalidInputException("Conversion date cannot be null");
        }

        ExchangeRate applicableRate = getExchangeRate(currencyCode, conversionDate);
        if (applicableRate == null) {
            logger.error("No exchange rate found for {} on {}", currencyCode, conversionDate);
            throw new ExchangeRateNotFoundException(currencyCode, conversionDate.toString());
        }

        // Correct conversion: amount in foreign / rate = amount in EUR
        BigDecimal result = foreignAmount.divide(applicableRate.getRate(), 6, RoundingMode.HALF_UP);
        logger.debug("Conversion successful: {} {} = {} EUR", foreignAmount, currencyCode, result);
        return result;
    }
}
