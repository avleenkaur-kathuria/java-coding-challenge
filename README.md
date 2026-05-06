# Crewmeister FX Rate Service

A Spring Boot microservice that provides foreign exchange rate information with data sourced from the German Bundesbank API.

## 🚀 Quick Start

### Prerequisites
- **Java 21** (OpenJDK 21 or later)
- **Maven 3.6+**
- **Internet connection** (for Bundesbank API data fetching)

### Build and Run

1. **Clone or download the project**
   ```bash
   cd java-coding-challenge
   ```

2. **Build the application**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Verify it's running**
   - Open browser: http://localhost:8080/api/v1/currencies
   - Should return: `["AUD","CAD","CHF","GBP","JPY","USD"]`

## 📡 API Endpoints

The service provides versioned REST endpoints. The current version is **v1** with **v2** available for enhanced features.

### API Versioning
- **Current Version:** v1 (`/api/v1/`)
- **Latest Version:** v2 (`/api/v2/`) - Enhanced features with metadata
- **Legacy Support:** v1 endpoints remain available for backward compatibility

### Version 1 Endpoints (Current)

#### Get Available Currencies
```http
GET /api/v1/currencies
```
**Response:** `["USD", "GBP", "JPY", "CHF", "CAD", "AUD"]`

#### Get All Exchange Rates
```http
GET /api/v1/rates
```
**Response:** Array of all exchange rate records with date, currency, and rate.

#### Get Rates for Specific Date
```http
GET /api/v1/rates/{date}
```
**Example:** `GET /api/v1/rates/2024-05-01`
**Response:** Exchange rates for all currencies on the specified date.

#### Convert Currency to EUR
```http
GET /api/v1/convert?from={currency}&amount={amount}&date={date}
```
**Example:** `GET /api/v1/convert?from=USD&amount=100&date=2024-05-01`
**Response:** Converted amount in EUR.

### Version 2 Endpoints (Enhanced)

#### Get Available Currencies (with metadata)
```http
GET /api/v2/currencies
```
**Response:** Enhanced currency list with country and region information.

#### Get All Exchange Rates (paginated)
```http
GET /api/v2/rates?page=0&size=50
```
**Response:** Paginated exchange rates with navigation metadata.

#### Convert Currency (enhanced response)
```http
GET /api/v2/convert?from={currency}&amount={amount}&date={date}&to=EUR
```
**Response:** Enhanced conversion result with metadata and rate information.

#### Bulk Currency Conversion
```http
POST /api/v2/convert/bulk
```
**Request Body:** Array of conversion requests
**Response:** Bulk conversion results with success/failure counts.

## 🧪 Testing the APIs

### Using curl:
```bash
# Version 1 endpoints
curl http://localhost:8080/api/v1/currencies
curl http://localhost:8080/api/v1/rates
curl http://localhost:8080/api/v1/rates/2024-05-01
curl "http://localhost:8080/api/v1/convert?from=USD&amount=100&date=2024-05-01"

# Version 2 endpoints (enhanced features)
curl http://localhost:8080/api/v2/currencies
curl "http://localhost:8080/api/v2/rates?page=0&size=10"
curl "http://localhost:8080/api/v2/convert?from=USD&amount=100&date=2024-05-01"
```

### Using browser:
- http://localhost:8080/api/v1/currencies
- http://localhost:8080/api/v1/rates
- http://localhost:8080/api/v1/rates/2024-05-01
- http://localhost:8080/api/v1/convert?from=USD&amount=100&date=2024-05-01
- http://localhost:8080/api/v2/currencies
- http://localhost:8080/api/v2/rates?page=0&size=10

### Interactive API Documentation:
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/api-docs

## 🏗️ Architecture

### Application Flow
1. **Startup:** Application fetches exchange rates from Bundesbank API
2. **Storage:** Data stored in H2 in-memory database
3. **API:** REST endpoints serve the stored data
4. **Fallback:** If API fails, generates sample data automatically

### Tech Stack
- **Framework:** Spring Boot 3.2.2
- **Language:** Java 21
- **Database:** H2 (in-memory)
- **HTTP Client:** Spring WebFlux WebClient
- **Build Tool:** Maven
- **Data Source:** Bundesbank API (German Central Bank)

### Project Structure
```
src/main/java/com/crewmeister/cmcodingchallenge/
├── CmCodingChallengeApplication.java    # Main application
└── currency/
    ├── CurrencyController.java          # REST API endpoints
    ├── ExchangeRate.java                # JPA entity
    ├── ExchangeRateRepository.java      # Data access
    └── ExchangeRateService.java         # Business logic
```

## 🔧 Configuration

### Application Properties
- **Server Port:** 8080
- **Database:** H2 in-memory (`jdbc:h2:mem:fxdb`)
- **JPA:** Auto-create tables, H2 dialect

### Data Source
- **Primary:** Bundesbank API (CSV format)
- **Fallback:** Random sample data (last 30 days)
- **Currencies:** USD, GBP, JPY, CHF, CAD, AUD

## 📊 Data Format

### Exchange Rates
- **Format:** EUR per unit of foreign currency
- **Example:** 1 USD = 0.85 EUR (rate = 0.85)
- **Precision:** 6 decimal places (financial accuracy)

### API Response Examples
```json
// /api/currencies
["USD", "GBP", "JPY", "CHF", "CAD", "AUD"]

// /api/convert?from=USD&amount=100&date=2024-05-01
92.165899
```

## 🚨 Troubleshooting

### Application won't start
- Ensure Java 21 is installed: `java -version`
- Ensure Maven is installed: `mvn -version`
- Check port 8080 is free: `lsof -i :8080`

### API returns empty data
- Check internet connection (Bundesbank API)
- Application falls back to sample data automatically
- Check logs for any error messages

### Build fails
- Run `mvn clean` first
- Ensure all dependencies download: `mvn dependency:resolve`

## 📝 Notes

- **Data Refresh:** Exchange rates are fetched once on startup
- **Persistence:** H2 is in-memory (data resets on restart)
- **API Source:** German Bundesbank (official ECB reference rates)
- **Error Handling:** Comprehensive logging and fallback mechanisms
- **Production Ready:** Includes validation, error handling, and proper HTTP status codes

## 🎯 User Stories Implemented

✅ **As a client, I want to get a list of all available currencies**
✅ **As a client, I want to get all EUR-FX exchange rates at all available dates as a collection**
✅ **As a client, I want to get the EUR-FX exchange rate at particular day**
✅ **As a client, I want to get a foreign exchange amount for a given currency converted to EUR on a particular day**

---

**Ready to run! 🚀** Just execute `mvn spring-boot:run` and visit http://localhost:8080/api/v1/currencies
