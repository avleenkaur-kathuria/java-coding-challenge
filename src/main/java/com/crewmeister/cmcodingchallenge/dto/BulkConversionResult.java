package com.crewmeister.cmcodingchallenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response object for bulk currency conversion operations.
 * Contains results for multiple conversion requests.
 */
@Schema(description = "Bulk currency conversion response")
public class BulkConversionResult {

    @Schema(description = "List of individual conversion results")
    private List<EnhancedConversionResult> conversions;

    @Schema(description = "Total number of conversion requests processed", example = "3")
    private int totalRequests;

    @Schema(description = "Number of successful conversions", example = "3")
    private long successfulConversions;

    @Schema(description = "Number of failed conversions", example = "0")
    private long failedConversions;

    @Schema(description = "Timestamp when the bulk operation was processed", example = "2024-05-01T10:30:00")
    private LocalDateTime processedAt;

    @Schema(description = "API version used", example = "v2")
    private String apiVersion;

    public BulkConversionResult(List<EnhancedConversionResult> conversions, int totalRequests,
                              long successfulConversions, long failedConversions,
                              LocalDateTime processedAt, String apiVersion) {
        this.conversions = conversions;
        this.totalRequests = totalRequests;
        this.successfulConversions = successfulConversions;
        this.failedConversions = failedConversions;
        this.processedAt = processedAt;
        this.apiVersion = apiVersion;
    }

    // Getters and setters
    public List<EnhancedConversionResult> getConversions() {
        return conversions;
    }

    public void setConversions(List<EnhancedConversionResult> conversions) {
        this.conversions = conversions;
    }

    public int getTotalRequests() {
        return totalRequests;
    }

    public void setTotalRequests(int totalRequests) {
        this.totalRequests = totalRequests;
    }

    public long getSuccessfulConversions() {
        return successfulConversions;
    }

    public void setSuccessfulConversions(long successfulConversions) {
        this.successfulConversions = successfulConversions;
    }

    public long getFailedConversions() {
        return failedConversions;
    }

    public void setFailedConversions(long failedConversions) {
        this.failedConversions = failedConversions;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }
}
