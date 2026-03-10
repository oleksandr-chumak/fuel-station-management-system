# Fuel Station Management System

A full-stack application for managing fuel stations, fuel orders, pricing, and personnel. It consists of a Spring Boot REST/WebSocket API, two Angular front-end apps, and a shared Angular SDK.

## Repository Layout

```
fuel-station-management-system/
├── fsms-backend-api/        # Spring Boot 4 REST + WebSocket API
├── fsms-admin/              # Angular admin dashboard (port 4200)
├── fsms-manager/            # Angular manager app (port 4201)
└── fsms-client-sdk/         # Angular workspace with shared SDK libraries
    └── projects/
        ├── fsms-web-api/    # REST + STOMP clients for all API domains
        └── fsms-security/   # Auth service, JWT guards, login UI components
```

## Services at a Glance

| Service | Type | Port | Description |
|---|---|---|---|
| `fsms-backend-api` | Spring Boot API | 8080 | REST endpoints + WebSocket (STOMP) |
| `fsms-admin` | Angular app | 4200 | Administrator dashboard |
| `fsms-manager` | Angular app | 4201 | Fuel station manager portal |
| PostgreSQL | Database | 5432 | Primary data store |
| Mailpit | Mail catcher | 1025/8025 | Local SMTP + web UI |

## Prerequisites

- **Java 25**
- **Node.js 22+** and **npm 11+**
- **Docker** and **Docker Compose**

## Quick Start

### 1. Start infrastructure (PostgreSQL + Mailpit)

```bash
cd fsms-backend-api
docker compose up -d
```

### 2. Start the backend API

```bash
cd fsms-backend-api
./gradlew bootRun
# API available at http://localhost:8080
```

### 3. Install SDK dependencies

```bash
cd fsms-client-sdk
npm install
```

### 4. Start the admin app

```bash
cd fsms-admin
npm install
npm start
# App available at http://localhost:4200
```

### 5. Start the manager app

```bash
cd fsms-manager
npm install
npm start
# App available at http://localhost:4201
```

## Sub-project Documentation

- [Backend API](fsms-backend-api/README.md)
- [Admin App](fsms-admin/README.md)
- [Manager App](fsms-manager/README.md)
- [Client SDK workspace](fsms-client-sdk/README.md)
  - [fsms-web-api library](fsms-client-sdk/projects/fsms-web-api/README.md)
  - [fsms-security library](fsms-client-sdk/projects/fsms-security/README.md)
