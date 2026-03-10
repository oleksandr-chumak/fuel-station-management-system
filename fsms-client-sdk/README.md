# fsms-client-sdk

Angular workspace containing the shared SDK libraries for the Fuel Station Management System front-end apps.

## Libraries

| Library | Package | Description |
|---|---|---|
| `fsms-web-api` | `projects/fsms-web-api/` | Typed REST + STOMP clients for all API domains |
| `fsms-security` | `projects/fsms-security/` | Auth service, JWT guards, login UI components |

See the individual library READMEs for detailed API references:
- [fsms-web-api](projects/fsms-web-api/README.md)
- [fsms-security](projects/fsms-security/README.md)

## Workspace Setup

```bash
npm install
```

## Building the Libraries

```bash
# Build the REST/STOMP client library
ng build fsms-web-api

# Build the security/auth library
ng build fsms-security
```

Build artifacts are placed in `dist/`.

## How Libraries Are Consumed

Both libraries are declared as local workspace packages in the consumer apps (`fsms-admin`, `fsms-manager`). They are resolved directly from the `dist/` directory — **no npm publish step is needed**.

After building, the apps pick up the latest output automatically when you run `npm install` in the app directory.

## Running Tests

```bash
npm test
```

Tests run with [Vitest](https://vitest.dev/).
