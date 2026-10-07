# NinjaBank - Account Service

Independent Spring Boot microservice for handling accounts and balances in the NinjaBank ecosystem.

## Features
- **Account Management**: Retrieve account details (`GET /accounts/{accountNumber}`)
- **Deposit**: Deposit funds to account (`POST /accounts/{accountNumber}/deposit`)
- **Withdrawal**: Withdraw funds with balance validation (`POST /accounts/{accountNumber}/withdraw`)
- **Security**: JWT-based stateless authentication (`Bearer` token)
- **Database**: PostgreSQL with Hibernate / Spring Data JPA
- **Status Validation**: Enforces account lifecycle (`ACTIVE`, `BLOCKED`, `CLOSED`)

## Tech Stack
- Java 17
- Spring Boot 3.5.x (Web, Data JPA, Security, Validation)
- PostgreSQL
- JJWT (0.11.5)
- Lombok

## Environment Configuration
Create a `.env` file in the root directory:
```properties
PORT=8083
DB_URL=jdbc:postgresql://<host>:5432/<database>?sslmode=require
DB_USERNAME=<username>
DB_PASSWORD=<password>
JWT_SECRET=<256-bit-secret>
```

## Running the Application
```bash
./mvnw clean spring-boot:run
```

## Running Tests
```bash
./mvnw test
```
