# fsms-admin

Angular administrator dashboard for the Fuel Station Management System.

## Tech Stack

| Technology | Version |
|---|---|
| Angular | 21.1.3 |
| PrimeNG | 21.1.1 |
| Tailwind CSS | 4.1.18 |
| RxJS | 7.8.2 |
| @stomp/rx-stomp | 2.3.0 |

## Features

- Manage fuel stations (create, deactivate, assign/unassign managers)
- Manage manager accounts (create, terminate)
- View and manage fuel orders (confirm/reject)
- Update fuel prices per station
- Monitor fuel tank levels
- Real-time event timeline per fuel station (unified fuel station + fuel order events)

## Prerequisites

- Node.js 22+ and npm 11+
- Backend API running on port 8080
- `fsms-client-sdk` dependencies installed (run `npm install` in `fsms-client-sdk/` first)

## Getting Started

### 1. Install dependencies

```bash
npm install
```

### 2. Configure the app

Edit `public/app.config.json`:

```json
{
  "restApiUrl": "http://localhost:8080",
  "stompApiUrl": "ws://localhost:8080/ws",
  "managerUrl": "http://localhost:4201"
}
```

### 3. Start the dev server

```bash
npm start
# App available at http://localhost:4200
```

## Runtime Configuration

The app reads `public/app.config.json` at startup — no rebuild needed when changing URLs:

| Key | Description |
|---|---|
| `restApiUrl` | Base URL for the REST API |
| `stompApiUrl` | WebSocket URL for STOMP events |
| `managerUrl` | URL of the manager app (used for cross-app links) |

## Project Structure

```
src/app/
├── modules/
│   ├── common/          # AppConfigService, LoggerService, CommandHandler base, dialogs
│   ├── fuel-orders/     # Fuel order components and handlers
│   ├── fuel-stations/   # Fuel station components, handlers, stores
│   ├── managers/        # Manager components and handlers
│   └── ui/              # Shared UI components
└── pages/
    ├── login/           # Login page
    ├── managers/        # Manager list page
    ├── fuel-orders/     # Fuel order list page
    └── fuel-stations/   # Fuel station list page
        └── [id]/        # Station detail with tabs:
            ├── fuel-station-info/
            ├── fuel-station-managers/
            ├── fuel-station-fuel-prices/
            ├── fuel-station-fuel-tanks/
            ├── fuel-station-fuel-orders/
            └── fuel-station-events/    # Unified event timeline
```

## Available Scripts

| Script | Description |
|---|---|
| `npm start` | Dev server on port 4200 (auto-reload) |
| `npm run build` | Production build to `dist/` |
| `npm run watch` | Incremental dev build |
| `npm test` | Unit tests with Karma |
| `npm run lint` | ESLint |

## SDK Dependencies

This app consumes two local workspace packages from `fsms-client-sdk/`:

- **`fsms-web-api`** — Typed REST clients and STOMP event clients for all API domains
- **`fsms-security`** — `AuthService`, `adminGuard`, `LoginFormComponent`, JWT interceptor
