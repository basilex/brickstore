# Brickstore

Brickstore is a Spring Boot RESTful API service that implements a catalog and administrative APIs for a small e-commerce/store platform. The codebase follows modern Spring Boot practices (Spring Boot 3.x, Java 21), uses Gradle (Kotlin DSL) for build, Flyway for database migrations, and adopts RFC7807 Problem Details for API error responses.

---

## Table of contents

- Project overview
- Key features
- Tech stack
- Getting started
  - Requirements
  - Build and run (local)
  - Run with Docker (development)
- Configuration and profiles
- Error contract (Problem Details)
- API documentation
- Tests
- Contributing
- License

---

## Project overview

This repository implements the Brickstore backend service. It provides REST endpoints for domain entities (countries, currencies, users, roles, etc.), integrates database migrations with Flyway, and includes full test coverage using JUnit 5 + Spring Test (MockMvc). The project aims to be a small, well-documented platform to demonstrate best-practice patterns for a Spring REST API.

## Key features

- RESTful API built with Spring Boot and Spring MVC
- RFC7807-compliant error responses (Problem Details) with compatibility fields for existing clients
- Request tracing via `X-Request-Id` and inclusion of `requestId` and `instance` (URN) in error responses
- Domain-specific `type` URIs derived from `ErrorCode` values (e.g. `/problems/country-not-found`)
- Flyway migrations for schema management (test and main migrations separated)
- OpenAPI / Swagger UI available via springdoc
- Gradle wrapper included for reproducible builds

## Tech stack

- Java 21
- Spring Boot 3.x (Spring MVC)
- Gradle (Kotlin DSL)
- Flyway (DB migrations)
- H2 for tests, Postgres used as default dev DB
- JUnit 5, Spring Test (MockMvc)
- springdoc-openapi for API docs

## Getting started

These instructions help you run the service locally for development and testing.

### Requirements

- JDK 21
- Git
- Docker (optional, recommended for DB/dev environment)
- Gradle wrapper is included; you can use `./gradlew` (macOS/Linux) or `gradlew.bat` (Windows)

### Build and run (local)

1. Clone the repository:

```bash
git clone git@github.com:basilex/brickstore.git
cd brickstore
```

2. Build and run tests:

```bash
./gradlew clean test
```

3. Run the application (dev profile):

```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

By default the application reads configuration from `src/main/resources/application.yaml` and profile overrides in `application-dev.yaml`.

### Run with Docker (development)

Run the Postgres database and application using Docker Compose (development):

```bash
docker compose -f docker-compose.dev.yml up --build
```

This starts dependent services defined in `docker-compose.dev.yml` and the application image.

## Configuration and profiles

- `application.yaml` contains shared configuration.
- `application-dev.yaml` and `application-test.yaml` contain profile-specific overrides.
- Tests run with the `test` profile (H2 in-memory DB and test-specific Flyway migrations).

## Error contract (Problem Details)

Brickstore uses RFC7807 Problem Details for error responses and provides backward-compatible fields to avoid breaking clients. Important notes:

- `type`: a URI identifying the problem type. For domain errors we derive a stable local path from `ErrorCode` values (e.g. `COUNTRY_NOT_FOUND` -> `/problems/country-not-found`). For unknown errors this defaults to `about:blank`.
- `title` / `status`: HTTP status and a short title
- `detail` / `details`: human-readable message(s)
- `instance`: set to `urn:uuid:<requestId>` when a request id is available
- `requestId`: the `X-Request-Id` used for request correlation
- Legacy fields such as `status`, `error`, `details`, and `timestamp` are preserved in responses for compatibility with existing clients and tests.

Example error response (404 Not Found):

```json
{
  "type": "/problems/country-not-found",
  "title": "Not Found",
  "status": 404,
  "detail": "Country with id 123 not found",
  "requestId": "b7e3b2f9-...",
  "instance": "urn:uuid:b7e3b2f9-...",
  "timestamp": "2025-12-14T...",
  "errorCode": "COUNTRY_NOT_FOUND"
}
```

See `src/main/java/com/platform/brickstore/api/exception/ProblemErrorAttributes.java` and `GlobalExceptionHandler.java` for the concrete mapping logic.

## API documentation

OpenAPI documentation is available via springdoc. When the application is running in dev mode, visit:

- Swagger UI: `http://localhost:8080/swagger-ui.html` (or `/swagger-ui/index.html` depending on springdoc version)
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Tests

Run unit and integration tests locally:

```bash
./gradlew clean test
```

Tests use H2 and test-specific Flyway migrations located under `src/test/resources/db/test-migration`.

There are integration tests verifying the `/error` contract and Problem Details formatting.

## Contributing

Contributions are welcome. A suggested workflow:

1. Create a feature branch from `dev` (named `feature/...` or `chore/...`).
2. Run tests and ensure they pass locally.
3. Push the branch and open a Pull Request against `dev`.
4. Add a clear PR description and link related issues.

Please keep commits focused and tests passing. If you change the error contract, update `docs/API_ERRORS.md` and the changelog.

## Where to look in the codebase

- `src/main/java/com/platform/brickstore/api/exception/GlobalExceptionHandler.java` — application exception handlers producing `ProblemDetail` responses.
- `src/main/java/com/platform/brickstore/api/exception/ProblemErrorAttributes.java` — custom `ErrorAttributes` shaping `/error` output.
- `src/main/java/com/platform/brickstore/api/filter/RequestIdFilter.java` — request id propagation and MDC integration.
- `src/main/java/com/platform/brickstore/api/exception/ErrorCode.java` — canonical error codes used to derive `type` URIs.
- `src/test/java` — tests including `ErrorAttributesIntegrationTest`.

## Changelog and docs

- See `CHANGELOG.md` for high-level changes.
- See `docs/API_ERRORS.md` for more details about the Problem Details migration and canonical `type` URIs.

## License

See the `LICENSE` file at the project root.

---

If you'd like, I can adapt this README to include badges (build, coverage), example requests/responses for common endpoints, or a quickstart Docker Compose recipe. Which would you prefer next?

# brickstore

Spring Restful API service

## Changelog (high level)

- 2025-12-14: Migrate API error responses to RFC7807 Problem Details. The application now returns RFC7807-style JSON for exceptions and the default `/error` endpoint, while preserving legacy fields (`status`, `error`, `details`, `timestamp`) for backward compatibility. Responses include a `requestId` and `instance` (URN) for request correlation. Domain-specific `type` URIs are generated from `ErrorCode` values (for example `COUNTRY_NOT_FOUND` -> `/problems/country-not-found`). See `docs/API_ERRORS.md` and `CHANGELOG.md` for more details.
