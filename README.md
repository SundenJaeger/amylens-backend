# AmyLens Backend

> **Module 3 — Automated Anomaly Detection & Data Management**  
> Spring Boot REST API server for the AmyLens rice grain amylose classification system.

---

## What This Module Does

This is the server component of AmyLens. It sits in the middle of the system and is responsible for:

- Receiving classification session records from the Android mobile app (Module 2)
- Validating that only approved devices can submit data
- Running automated statistical anomaly detection on each session using historical GQ-RIS data
- Serving authorized researcher lists and variety lists to the mobile app
- Providing all data to the web dashboard (Module 4) for review and analytics
- Generating CSV, JSON, and PDF exports conforming to the 14-column GQ-RIS schema

It does **not** do any AI training or image processing. That is Module 2's responsibility.

---

## Tech Stack

| Layer | Technology                                        |
|---|---------------------------------------------------|
| Framework | Spring Boot 4 (Java 25)                           |
| Security | Spring Security (session-based, HttpOnly cookies) |
| Database | PostgreSQL                                        |
| ORM | Spring Data JPA (Hibernate)                       |
| Cache | Caffeine via Spring Cache                         |
| PDF Export | iText 7                                           |
| CSV Export | OpenCSV                                           |
| API Docs | SpringDoc OpenAPI (Swagger UI)                    |
| Deployment | Render                                            |

---

## Getting Started

### Prerequisites

- Java 25
- Maven
- PostgreSQL running locally

### 1. Clone and switch to dev branch

```bash
git clone https://github.com/SundenJaeger/amylens-backend.git
cd amylens-backend
git checkout dev
```

### 2. Configure your database

Create a PostgreSQL database named `amylens`:

```sql
CREATE DATABASE amylens;
```

Then update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/amylens
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Run the application

```bash
./mvnw spring-boot:run
```

On first startup the server will:
- Auto-create all database tables via Hibernate
- Seed 10 rice varieties into the varieties table
- Seed GQ-RIS historical mirror data for all seeded varieties

### 4. View API documentation

```
http://localhost:8080/swagger-ui/index.html
```

All endpoints are fully documented with request/response schemas.
---

## Endpoint Overview

| Method | Path | Caller | Auth Required |
|---|---|---|---|
| POST | `/api/devices/register` | Module 1 | No |
| POST | `/api/devices/auth` | Module 1 | No |
| GET | `/api/devices/{ssaid}/users` | Module 2 | No |
| GET | `/api/varieties` | Module 2 | No |
| POST | `/api/sessions` | Module 2 | No |
| GET | `/api/devices` | Module 4 | Yes |
| GET | `/api/devices/researchers` | Module 4 | No |
| PUT | `/api/devices/{id}/approve` | Module 4 | Yes |
| PUT | `/api/devices/{id}/deny` | Module 4 | Yes |
| GET | `/api/sessions` | Module 4 | Yes |
| POST | `/api/sessions/{id}/review` | Module 4 | Yes |
| GET | `/api/sessions/export` | Module 4 | Yes |
| GET | `/api/analytics` | Module 4 | Yes |
| POST | `/api/admin/gqris/import` | Module 4 | Yes |
| POST | `/api/auth/login` | Module 4 | No |
| POST | `/api/auth/logout` | Module 4 | Yes |

Full request/response contracts are available on Swagger UI.

---

## Anomaly Detection Logic

For every submitted session the server runs this check automatically in the background:

```
if confidence_score < 0.85
    → needs_review (low_confidence)

else if historical records for variety < 3
    → needs_review (insufficient_data)

else if amylose ordinal outside mean ± 2 standard deviations
    → needs_review (deviation)

else
    → verified
```

Amylose ordinal mapping: `Waxy=1, Low=2, Intermediate=3, High=4`

---

## Database Tables

| Table | Description |
|---|---|
| `devices` | Registered Android devices with approval status and linked researchers |
| `sessions` | Classification session records submitted by the mobile app |
| `varieties` | Registered rice varieties served to the mobile app dropdown |
| `gqris_mirror` | Read-only historical GQ-RIS amylose data used for anomaly detection |
| `device_user_names` | Join table for device-to-researcher mappings |

---

## Running Tests

```bash
./mvnw test
```

Test coverage includes:
- `DeviceAuthApiTest` — device registration and authorization checks
- `SessionIngestionApiTest` — session payload validation
- `AnomalyDetectionEngineTest` — anomaly detection logic unit tests

## Live Deployment

The backend is deployed on Render:

```
https://amylens-backend.onrender.com/
```

Swagger UI:
```
https://amylens-backend.onrender.com/swagger-ui/index.html
```