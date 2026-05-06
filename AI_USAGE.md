# AI Usage in This Project

Yeah, I used GitHub Copilot a fair amount on this currency exchange rate app. Helped speed things up, especially the  repetitive coding bits.

## How I Used AI Tools

Used Copilot for initial project setup - asked it to create a basic Spring Boot structure with H2 database and REST APIs for currency exchange rates. Got the foundation with dependencies and config, then customized everything for the Bundesbank integration.

For the database entity, asked Copilot for JPA entity structure with annotations. Used the basic @Entity setup but implemented all the Bundesbank-specific data handling myself.

Copilot helped with basic CSV parsing concepts, but I built the actual Bundesbank CSV parser from scratch to handle German decimal formats and error cases.

Got basic currency conversion math from Copilot, but implemented the complete business logic with all edge cases and API fallbacks manually.

## My Take on Using AI

I see Copilot as a coding buddy that handles the grunt work - autocompleting, suggesting method names, reminding me of syntax. Great for cutting down boilerplate and catching dumb typos. But the important bits - business logic, Bundesbank API integration, error scenarios, data transformation - that's all me.

This way I actually understand what's going on. If something breaks, I know exactly why because I built it. AI made me faster, but the real thinking and implementation is mine.

## What Got AI Help vs Manual Work

AI pitched in on:
- Basic project setup and dependencies
- Config files like application.properties
- Skeleton entity and repository classes
- REST controller method signatures

Everything else I did by hand:
- All the business logic in ExchangeRateService.java
- Bundesbank CSV parsing with German decimal handling
- Error handling and fallback data generation
- API responses and validation
- Database query optimization

