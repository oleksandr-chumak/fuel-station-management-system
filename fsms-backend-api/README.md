# fsms-backend-api

Spring Boot REST and WebSocket API for the Fuel Station Management System.

## Tech Stack

| Technology | Version |
|---|---|
| Java | 25 |
| Spring Boot | 4.0.2 |
| Spring Security (JWT) | via jjwt 0.12.5 |
| Spring WebSocket (STOMP) | included |
| Hibernate / JPA | 7 |
| Liquibase | included |
| PostgreSQL | 13 |
| Lombok | latest |

## Architecture

The project follows a **domain-driven / hexagonal architecture** with five bounded contexts:

```
src/main/java/com/fuelstation/managmentapi/
├── administrator/     # Admin user domain
├── authentication/    # JWT auth, login use cases, security config
├── common/            # Shared value objects, base classes
├── fuelorder/         # Fuel order lifecycle (create/confirm/reject)
├── fuelstation/       # Fuel stations, prices, tanks, events
└── manager/           # Manager accounts and fuel station assignments
```

Each domain uses three layers: `domain` → `application` → `infrastructure`.

## Prerequisites

- Java 25
- Docker (for PostgreSQL and Mailpit)

## Getting Started

### 1. Start infrastructure

```bash
docker compose up -d
```

This starts:
- **PostgreSQL 13** on port `5432`
- **Mailpit** SMTP on port `1025`, web UI on port `8025`

### 2. Build

```bash
./gradlew build
```

### 3. Run

```bash
./gradlew bootRun
# or
java -jar build/libs/*.jar
```

The API is available at `http://localhost:8080`.

## Configuration

Key settings in `src/main/resources/application.yml`:

| Key | Default | Description |
|---|---|---|
| `spring.datasource.url` | `jdbc:postgresql://localhost:5432/fuelstation` | Database URL |
| `spring.datasource.hikari.maximum-pool-size` | `10` | Connection pool size |
| `spring.mail.host` | `localhost` | SMTP host |
| `spring.mail.port` | `1025` | SMTP port (Mailpit) |
| `app.jwt.secret` | (base64 key) | 256-bit JWT signing secret |
| `app.jwt.access-token-expiry` | `15m` | Access token lifetime |
| `app.jwt.refresh-token-expiry` | `7d` | Refresh token lifetime |
| `app.cors.allowed-origins` | `http://localhost:4200,4201` | CORS origins |

## REST API Reference

### Authentication — `/api/auth`

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/auth/admins/login` | Authenticate as administrator |
| `POST` | `/api/auth/managers/login` | Authenticate as manager |
| `GET` | `/api/auth/managers/{managerId}/token` | Get manager access token |
| `GET` | `/api/auth/me` | Get current authenticated user |

### Fuel Stations — `/api/fuel-stations`

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/fuel-stations` | Create fuel station |
| `PUT` | `/api/fuel-stations/{id}/deactivate` | Deactivate fuel station |
| `PUT` | `/api/fuel-stations/{id}/assign-manager` | Assign manager to station |
| `PUT` | `/api/fuel-stations/{id}/unassign-manager` | Unassign manager from station |
| `PUT` | `/api/fuel-stations/{id}/change-fuel-price` | Update fuel price |
| `GET` | `/api/fuel-stations/{id}` | Get fuel station by ID |
| `GET` | `/api/fuel-stations` | List all fuel stations |
| `GET` | `/api/fuel-stations/{id}/managers` | List managers assigned to station |
| `GET` | `/api/fuel-stations/{id}/fuel-orders` | List fuel orders for station |
| `GET` | `/api/fuel-stations/{id}/events` | Get merged event timeline (cursor paginated) |

The events endpoint returns the last 10 events when called without parameters. Pass `?occurredAfter=<ISO-instant>` to page forward in time (sorted ASC).

### Managers — `/api/managers`

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/managers` | Create manager account |
| `PUT` | `/api/managers/{id}/terminate` | Terminate manager |
| `GET` | `/api/managers` | List all managers |
| `GET` | `/api/managers/{id}` | Get manager by ID |
| `GET` | `/api/managers/{id}/fuel-stations` | List stations assigned to manager |

### Fuel Orders — `/api/fuel-orders`

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/fuel-orders` | Create fuel order |
| `PUT` | `/api/fuel-orders/{id}/confirm` | Confirm fuel order |
| `PUT` | `/api/fuel-orders/{id}/reject` | Reject fuel order |
| `GET` | `/api/fuel-orders` | List all fuel orders |
| `GET` | `/api/fuel-orders/{id}` | Get fuel order by ID |

## WebSocket Topics (STOMP)

| Topic | Description |
|---|---|
| `/topic/fuel-stations/{id}/**` | All events for a fuel station |
| `/topic/fuel-stations/{id}/price-changed` | Fuel price updated |
| `/topic/fuel-stations/{id}/manager-assigned` | Manager assigned |
| `/topic/fuel-stations/{id}/manager-unassigned` | Manager unassigned |
| `/topic/fuel-orders/{id}/**` | All events for a fuel order |
| `/topic/managers/{id}/assigned-to-fuel-station` | Manager received assignment |
| `/topic/managers/{id}/unassigned-from-fuel-station` | Manager lost assignment |

Connect to the STOMP endpoint at `ws://localhost:8080/ws`.

## Database Migrations

Managed by Liquibase. Migration files are in `src/main/resources/db/changelog/`:

| File | Description |
|---|---|
| `001-create-users-table.sql` | Users (admins + managers) |
| `002-create-fuel-stations-table.sql` | Fuel stations |
| `003-create-fuel-stations-managers-table.sql` | Station–manager assignments |
| `004-create-fuel-station-fuel-prices-table.sql` | Fuel price history |
| `005-create-fuel-station-fuel-tanks-table.sql` | Fuel tanks |
| `006-create-fuel-orders-table.sql` | Fuel orders |
| `007-create-fuel-station-events-table.sql` | Fuel station domain events |
| `008-create-fuel-order-events-table.sql` | Fuel order domain events |

## Running Tests

```bash
./gradlew test
```

Tests use an in-memory H2 database and Testcontainers where needed — no running services required.
