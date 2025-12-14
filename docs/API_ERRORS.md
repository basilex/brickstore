# API Errors (short)

- Format: API error responses are returned as RFC7807 `ProblemDetail` objects (see https://datatracker.ietf.org/doc/html/rfc7807).
- Compatibility: The old `ErrorResponse` DTO was removed from the codebase to avoid duplication. `GlobalExceptionHandler` still exposes a few legacy properties (`status`, `error`, `details`, `timestamp`) inside the `ProblemDetail` body to keep existing tests and clients working.
- Request tracing: Error responses include a `requestId` property and `instance` (URN) derived from the `X-Request-Id` HTTP header for easier correlation.

If you maintain clients, consider switching to consume `ProblemDetail` fields (`type`, `title`, `status`, `detail`, `instance`) and the new `requestId` as the canonical correlation id.

## Example ProblemDetail response (JSON)

```json
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "Country not found with pid: non_existing_path",
  "instance": "urn:uuid:fbb511a3-e4c6-4eb4-9417-a408f8e5b8e9",
  "timestamp": "2025-12-14T20:21:55.997898Z",
  "error": "Not Found",
  "details": { "errorCode": "COUNTRY_NOT_FOUND" },
  "errorCode": "COUNTRY_NOT_FOUND",
  "requestId": "fbb511a3-e4c6-4eb4-9417-a408f8e5b8e9"
}
```

