# 🥷 NinjaBank - Account Microservice

Production-ready, cloud-native **Core Account & Ledger Microservice** for the **NinjaBank Platform** built with **Spring Boot 3.5**, **Spring Security 6 (Stateless JWT)**, and **PostgreSQL (Neon)**.

---

## 🌟 Core Features

- **💳 Account Management**:
  - Retrieve real-time account balances and metadata (`GET /accounts/{accountNumber}`).
  - Account status validation (`ACTIVE`, `FROZEN`, `CLOSED`).
- **💰 Atomic Ledger Operations**:
  - Deposit funds into accounts (`POST /accounts/{accountNumber}/deposit`).
  - Withdraw funds with atomic balance checks (`POST /accounts/{accountNumber}/withdraw`).
  - Double-entry ledger money transfers (`POST /accounts/transfer`).
- **🔐 Security**:
  - Stateless JWT authentication via Bearer tokens.
  - Granular CORS configuration for multi-service cross-origin communication.
- **⚡ Health Monitoring**:
  - Public `/health` endpoint for Docker and Render health probes.

---

## 📖 API Endpoints

| Method | Endpoint | Access | Description |
| :--- | :--- | :---: | :--- |
| `GET` | `/health` | Public | Service health verification |
| `GET` | `/accounts/{accountNumber}` | Bearer Token | Retrieve account details & balance |
| `POST` | `/accounts/{accountNumber}/deposit` | Bearer Token | Deposit funds into specified account |
| `POST` | `/accounts/{accountNumber}/withdraw` | Bearer Token | Withdraw funds from specified account |
| `POST` | `/accounts/transfer` | Bearer Token | Execute atomic balance transfer |

---

## ⚙️ Environment Configuration

| Variable | Default Value | Description |
| :--- | :--- | :--- |
| `PORT` | `8083` | Server port |
| `DB_URL` | `jdbc:postgresql://localhost:5432/ninjabank_account` | PostgreSQL connection URL |
| `DB_USERNAME` | `postgres` | Database username |
| `DB_PASSWORD` | `postgres` | Database password |
| `JWT_SECRET` | *(256-bit secret)* | Secret key for JWT signing & verification |

---

## 🚀 Running Locally

```bash
# Set environment variables (or create .env file)
.\mvnw.cmd clean spring-boot:run
```

---

## 🐳 Docker Deployment

```bash
# Build Docker image
docker build -t ninjabank-account-service .

# Run container
docker run -d -p 8083:8083 --name ninjabank-account-service ninjabank-account-service
```
