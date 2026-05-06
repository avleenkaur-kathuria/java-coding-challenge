package com.crewmeister.cmcodingchallenge;

import com.crewmeister.cmcodingchallenge.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class for the Crewmeister Coding Challenge.
 * Implements CommandLineRunner to fetch and cache exchange rates on startup.
 */
@SpringBootApplication
public class CmCodingChallengeApplication implements CommandLineRunner {

    @Autowired
    private ExchangeRateService exchangeRateService;

    public static void main(String[] args) {
        SpringApplication.run(CmCodingChallengeApplication.class, args);
    }

    /**
     * Runs after the application context is loaded.
     * Fetches exchange rates from Bundesbank and stores them in the database.
     */
    @Override
    public void run(String... args) throws Exception {
        exchangeRateService.fetchAndSaveExchangeRates();
    }
}
