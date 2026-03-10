# fsms-security

Angular library providing authentication, JWT route guards, and login UI components for the Fuel Station Management System apps.

## Setup

Register `SecurityModule` in your app's providers:

```typescript
import { SecurityModule } from 'fsms-security';

// app.config.ts
export const appConfig: ApplicationConfig = {
  providers: [
    importProvidersFrom(SecurityModule),
    // ... other providers
  ],
};
```

`SecurityModule` registers `AuthInterceptor` globally, which automatically attaches the JWT access token to every outgoing HTTP request.

## Services

### `AuthService`

```typescript
import { AuthService } from 'fsms-security';
```

| Method | Description |
|---|---|
| `loginAdmin(req)` | Authenticate as administrator, store tokens |
| `loginManager(req)` | Authenticate as manager, store tokens |
| `logout()` | Clear stored tokens and redirect to login |
| `getUser()` | Get the current authenticated user (`User \| null`) |
| `getAccessToken()` | Get the raw JWT access token string |
| `isLoggedIn()` | Returns `true` if a valid token is present |

Tokens are stored in `localStorage` and read back on page refresh.

## Route Guards

```typescript
import { adminGuard, managerGuard } from 'fsms-security';
```

| Guard | Description |
|---|---|
| `adminGuard` | Allows access only to users with the `ADMINISTRATOR` role |
| `managerGuard` | Allows access only to users with the `MANAGER` role |

Both guards redirect to `/login` if the user is not authenticated or lacks the required role.

**Usage:**

```typescript
// app.routes.ts
export const routes: Routes = [
  {
    path: 'dashboard',
    canActivate: [adminGuard],
    component: DashboardPage,
  },
];
```

## Components

### `LoginFormComponent`

Reusable login form with username/password fields and a submit button. Emits a `login` event with credentials on submit.

```html
<fsms-login-form (login)="onLogin($event)" />
```

### `NotLoggedInHeaderComponent`

Minimal page header displayed on unauthenticated pages (e.g., the login page).

```html
<fsms-not-logged-in-header />
```

## Implementation Notes

- JWT tokens are stored in `localStorage` under well-known keys
- `AuthInterceptor` is an Angular `HttpInterceptor` that reads the access token and adds the `Authorization: Bearer <token>` header automatically
- Token expiry is not tracked client-side — the API returns 401 when a token expires, triggering a logout
