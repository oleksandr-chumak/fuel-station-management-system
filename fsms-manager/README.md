# fsms-manager

Angular app for fuel station managers in the Fuel Station Management System.

## Tech Stack

| Technology | Version |
|---|---|
| Angular | 21.1.3 |
| PrimeNG | 21.1.1 |
| Tailwind CSS | 4.1.18 |
| RxJS | 7.8.2 |
| @stomp/rx-stomp | 2.3.0 |

## Features

- View assigned fuel stations
- View station details (info, assigned managers, fuel prices, fuel tanks)
- Create and track fuel orders
- Real-time updates via WebSocket (fuel station events, assignment changes)

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
  "stompApiUrl": "ws://localhost:8080/ws"
}
```

### 3. Start the dev server

```bash
npm start
# App available at http://localhost:4201
```

## Runtime Configuration

The app reads `public/app.config.json` at startup — no rebuild needed when changing URLs:

| Key | Description |
|---|---|
| `restApiUrl` | Base URL for the REST API |
| `stompApiUrl` | WebSocket URL for STOMP events |

## Project Structure

```
src/app/
├── modules/
│   ├── common/          # AppConfigService, LoggerService, CommandHandler base
│   ├── fuel-order/      # Fuel order components
│   ├── fuel-station/    # Fuel station event handlers (STOMP)
│   ├── manager/         # ManagerStore, ManagerEventHandler (assignment events)
│   └── ui/              # Shared UI components
└── pages/
    ├── login/           # Login page
    └── fuel-stations/   # Assigned fuel stations list
        └── [id]/        # Station detail with tabs:
            ├── fuel-station-info/
            ├── fuel-station-managers/
            ├── fuel-station-fuel-prices/
            ├── fuel-station-fuel-tanks/
            └── fuel-station-fuel-orders/
```

## Available Scripts

| Script | Description |
|---|---|
| `npm start` | Dev server on port 4201 (auto-reload) |
| `npm run build` | Production build to `dist/` |
| `npm run watch` | Incremental dev build |
| `npm test` | Unit tests with Karma |
| `npm run lint` | ESLint |

## SDK Dependencies

This app consumes two local workspace packages from `fsms-client-sdk/`:

- **`fsms-web-api`** — Typed REST clients and STOMP event clients for all API domains
- **`fsms-security`** — `AuthService`, `managerGuard`, `LoginFormComponent`, JWT interceptor
