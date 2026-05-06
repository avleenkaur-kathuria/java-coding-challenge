package com.crewmeister.cmcodingchallenge.repository;

import com.crewmeister.cmcodingchallenge.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing ExchangeRate entities in the database.
 * Provides custom query methods for currency and date-based lookups.
 */
@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    /**
     * Finds all exchange rates for a specific currency.
     * @param currency The currency code
     * @return List of ExchangeRate entities
     */
    List<ExchangeRate> findByCurrency(String currency);

    /**
     * Finds all exchange rates for a specific date.
     * @param date The date
     * @return List of ExchangeRate entities for that date
     */
    List<ExchangeRate> findByDate(LocalDate date);

    /**
     * Finds the exchange rate for a specific currency and date.
     * @param currency The currency code
     * @param date The date
     * @return Optional containing the ExchangeRate if found
     */
    Optional<ExchangeRate> findByCurrencyAndDate(String currency, LocalDate date);

    /**
     * Retrieves all exchange rates ordered by date ascending.
     * @return List of all ExchangeRate entities sorted by date
     */
    List<ExchangeRate> findAllByOrderByDateAsc();

    /**
     * Retrieves distinct currency codes from the database.
     * Uses a custom JPQL query for efficiency.
     * @return List of unique currency codes
     */
    @Query("SELECT DISTINCT e.currency FROM ExchangeRate e ORDER BY e.currency")
    List<String> findDistinctCurrency();
}


