package com.crewmeister.cmcodingchallenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

/**
 * Enhanced currency metadata response for API v2.
 * Provides additional information about available currencies.
 */
@Schema(description = "Enhanced currency list with metadata")
public class CurrencyMetadataResponse {

    @Schema(description = "List of available currency codes", example = "[\"USD\", \"GBP\", \"JPY\", \"CHF\", \"CAD\", \"AUD\"]")
    private java.util.List<String> currencies;

    @Schema(description = "Detailed information about each currency")
    private Map<String, CurrencyInfo> currencyInfo;

    @Schema(description = "Total number of available currencies", example = "6")
    private int totalCount;

    @Schema(description = "API version", example = "v2")
    private String apiVersion;

    public CurrencyMetadataResponse(java.util.List<String> currencies, Map<String, CurrencyInfo> currencyInfo, int totalCount, String apiVersion) {
        this.currencies = currencies;
        this.currencyInfo = currencyInfo;
        this.totalCount = totalCount;
        this.apiVersion = apiVersion;
    }

    // Getters and setters
    public java.util.List<String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(java.util.List<String> currencies) {
        this.currencies = currencies;
    }

    public Map<String, CurrencyInfo> getCurrencyInfo() {
        return currencyInfo;
    }

    public void setCurrencyInfo(Map<String, CurrencyInfo> currencyInfo) {
        this.currencyInfo = currencyInfo;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    /**
     * Currency information details
     */
    @Schema(description = "Detailed currency information")
    public static class CurrencyInfo {
        @Schema(description = "Full currency name", example = "US Dollar")
        private String name;

        @Schema(description = "Country of origin", example = "United States")
        private String country;

        @Schema(description = "Geographic region", example = "America")
        private String region;

        public CurrencyInfo(String name, String country, String region) {
            this.name = name;
            this.country = country;
            this.region = region;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }
    }
}
