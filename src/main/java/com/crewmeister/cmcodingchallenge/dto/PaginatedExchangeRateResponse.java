package com.crewmeister.cmcodingchallenge.dto;

import com.crewmeister.cmcodingchallenge.entity.ExchangeRate;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * Paginated response for exchange rates in API v2.
 * Provides pagination metadata along with the data.
 */
@Schema(description = "Paginated exchange rate response")
public class PaginatedExchangeRateResponse {

    @Schema(description = "List of exchange rates for the current page")
    private List<ExchangeRate> content;

    @Schema(description = "Current page number (0-based)", example = "0")
    private int pageNumber;

    @Schema(description = "Page size", example = "50")
    private int pageSize;

    @Schema(description = "Total number of elements across all pages", example = "150")
    private int totalElements;

    @Schema(description = "Total number of pages", example = "3")
    private int totalPages;

    @Schema(description = "Whether there is a next page", example = "true")
    private boolean hasNext;

    @Schema(description = "Whether there is a previous page", example = "false")
    private boolean hasPrevious;

    public PaginatedExchangeRateResponse(List<ExchangeRate> content, int pageNumber, int pageSize,
                                       int totalElements, int totalPages, boolean hasNext, boolean hasPrevious) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }

    // Getters and setters
    public List<ExchangeRate> getContent() {
        return content;
    }

    public void setContent(List<ExchangeRate> content) {
        this.content = content;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }
}
