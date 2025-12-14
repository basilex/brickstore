API Errors (short)

- Format: API error responses are returned as RFC7807 `ProblemDetail` objects (see https://datatracker.ietf.org/doc/html/rfc7807).
- Compatibility: The old `ErrorResponse` DTO was removed from the codebase to avoid duplication. `GlobalExceptionHandler` still exposes a few legacy properties (`status`, `error`, `details`, `timestamp`) inside the `ProblemDetail` body to keep existing tests and clients working.
- Request tracing: Error responses include a `requestId` property and `instance` (URN) derived from the `X-Request-Id` HTTP header for easier correlation.

If you maintain clients, consider switching to consume `ProblemDetail` fields (`type`, `title`, `status`, `detail`, `instance`) and the new `requestId` as the canonical correlation id.
