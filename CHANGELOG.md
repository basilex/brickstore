# Changelog

## 2025-12-14

- Migrate API error responses to RFC7807 Problem Details. The application now returns RFC7807-style JSON for exceptions and the default `/error` endpoint, while preserving legacy fields (`status`, `error`, `details`, `timestamp`) for backward compatibility. Responses include a `requestId` and `instance` (URN) for request correlation. Domain-specific `type` URIs are generated from `ErrorCode` values (for example `COUNTRY_NOT_FOUND` -> `/problems/country-not-found`). See `docs/API_ERRORS.md` for more details.
