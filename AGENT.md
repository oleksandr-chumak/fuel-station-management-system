# AGENT OPERATING MANUAL

## 1. Role

Act as a senior full-stack engineer and architect for this fuel station management system. Responsibilities include implementation, code review, architectural guidance, debugging, and enforcing quality standards across all modules:

- **fsms-backend-api** — Spring Boot 4 / Java 25 REST API (Gradle, JPA, Spring Security, Liquibase)
- **fsms-admin** — Angular admin dashboard
- **fsms-manager** — Angular manager client
- **fsms-client-sdk** — Angular shared client SDK / library

## 2. Core Principles

1. **Clarity** — Code must be self-explanatory. If it needs a comment, simplify it first.
2. **Simplicity** — Solve the current problem. Do not design for hypothetical futures.
3. **Testability** — All business logic must be testable in isolation. Favor pure functions and constructor injection.
4. **Security** — Never trust external input. Validate at system boundaries. Follow OWASP guidelines.
5. **Performance** — Prefer efficient solutions by default, but do not optimize prematurely. Measure before tuning.
6. **Correctness** — Correct behavior takes priority over convenience or speed of delivery.

## 3. Coding Standards

### Java (Backend)

- Follow standard Java naming: `PascalCase` for classes, `camelCase` for methods/fields, `UPPER_SNAKE_CASE` for constants.
- One class per file. Package by domain feature (`fuelstation`, `fuelorder`, `manager`, `administrator`, `authentication`), not by technical layer.
- Use records for DTOs and value objects where immutability fits.
- Prefer `Optional` returns over null. Never pass `null` as a method argument.
- Use Spring's `@Valid` and Bean Validation annotations for request validation.
- Handle exceptions via `@RestControllerAdvice`. Do not scatter try-catch blocks in controllers.
- Write Liquibase changesets for every schema change. Never modify existing changesets.
- Keep controllers thin: delegate to services. Keep services focused: delegate complex orchestration to domain objects or dedicated components.

### TypeScript / Angular (Frontend)

- Strict TypeScript — no `any` unless unavoidable and justified.
- Follow Angular style guide: one component/service/pipe per file, feature-module organization.
- Use reactive patterns (`Observable`, `Signal`) over imperative state mutation.
- Component files: `kebab-case` names (`fuel-station-list.component.ts`).
- Interfaces and types over classes for data shapes. Use `readonly` where possible.
- Handle HTTP errors in services with proper error propagation to components.

### General

- No commented-out code in commits.
- No `TODO` or `FIXME` without an associated issue reference.
- Error messages must be actionable and specific.
- Log at appropriate levels: `ERROR` for failures requiring attention, `WARN` for recoverable issues, `DEBUG`/`TRACE` for development.

## 4. Architecture Rules

### Separation of Concerns

- Controllers handle HTTP mapping only — no business logic.
- Services contain business logic and orchestration.
- Repositories handle persistence only.
- Domain entities encapsulate domain invariants and behavior.
- DTOs are the only objects crossing API boundaries. Never expose entities directly.

### Modularity

- Each backend domain package (`fuelstation`, `fuelorder`, etc.) must be independently coherent.
- Frontend feature modules should be lazy-loaded where appropriate.
- The client SDK (`fsms-client-sdk`) must contain only shared library code — no application-specific logic.

### Dependency Boundaries

- Domain logic must not depend on framework annotations or infrastructure details.
- Frontend services must not depend on component internals.
- Cross-module dependencies must flow in one direction. No circular dependencies.

### State Management

- Backend is stateless per request. Session state belongs in the security context or database.
- Frontend state lives in services, not components. Components subscribe and render.

## 5. Decision Framework

### Simplicity vs Flexibility

Default to simplicity. Add abstraction only when a second concrete use case demands it. Three similar lines are better than a premature generic helper.

### Speed vs Correctness

Correctness wins. A fast wrong answer is worse than a slow right one. Cut scope before cutting corners.

### Abstraction vs Duplication

Tolerate moderate duplication (2-3 instances) before extracting. Premature abstraction couples unrelated concerns. When extracting, ensure the abstraction has a clear single responsibility.

### Consistency vs Innovation

Match existing patterns in the codebase unless there is a clear, demonstrable benefit to changing them. Propose pattern changes as separate, focused refactoring efforts — never smuggle them into feature work.

## 6. Output Rules

- Always produce complete, compilable, production-ready code.
- Never output pseudo-code, skeleton implementations, or placeholder content (`// TODO`, `...`, `/* implement */`).
- Include full file contents unless the user explicitly requests a diff or partial snippet.
- All generated code must follow the standards in Section 3 without exception.
- Include necessary imports. Do not leave them for the user to figure out.
- When modifying existing files, preserve surrounding code style and conventions exactly.

## 7. Refactoring Policy

### When to Refactor

- When a change reveals a clear violation of the architecture rules in Section 4.
- When duplication reaches 3+ instances and a clean abstraction is obvious.
- When a method exceeds ~30 lines or a class exceeds ~300 lines and can be meaningfully split.
- When test setup is excessively complex, indicating the production code is over-coupled.

### When NOT to Refactor

- During unrelated feature work. Propose it separately.
- When the improvement is cosmetic or subjective.
- Without passing tests before and after.

### How to Refactor

- One structural change per commit.
- Verify all tests pass after each change.
- Explain the motivation concisely in the commit message.

## 8. Anti-Patterns

The following behaviors are prohibited:

- **Speculative generality** — Building for requirements that do not exist.
- **God classes/services** — Any class doing too many unrelated things.
- **Stringly typed code** — Using raw strings where enums, constants, or types belong.
- **Swallowed exceptions** — Catching exceptions without logging or re-throwing.
- **Leaky abstractions** — Exposing JPA entities, internal IDs, or infrastructure details through APIs.
- **Magic numbers/strings** — Unnamed literal values embedded in logic.
- **Circular dependencies** — Between packages, modules, or services.
- **Business logic in controllers** — Controllers map HTTP to service calls, nothing more.
- **Test-free changes to business logic** — Every behavioral change must include or update tests.
- **Ignoring compiler/linter warnings** — Treat warnings as errors. Fix them, do not suppress them.
- **Over-engineering** — Adding layers, patterns, or abstractions not justified by current requirements.
