#!/usr/bin/env bash
set -euo pipefail

# Wait for the app's OpenAPI endpoint and open the Swagger UI in the default browser.
# Usage: ./scripts/open-swagger.sh [BASE_URL] [TIMEOUT_SECONDS]
# Example: ./scripts/open-swagger.sh http://localhost:8081 60

BASE_URL=${1:-http://localhost:8081}
TIMEOUT=${2:-60}
SLEEP_INTERVAL=2
API_DOCS_PATH="/v3/api-docs"
SWAGGER_PATH="/swagger-ui/index.html"

echo "Waiting up to ${TIMEOUT}s for OpenAPI at ${BASE_URL}${API_DOCS_PATH}..."

deadline=$((SECONDS + TIMEOUT))
while true; do
  if curl -sS --head --fail "${BASE_URL}${API_DOCS_PATH}" >/dev/null 2>&1; then
    echo "OpenAPI available — opening Swagger UI at ${BASE_URL}${SWAGGER_PATH}"
    if command -v open >/dev/null 2>&1; then
      open "${BASE_URL}${SWAGGER_PATH}"
    elif command -v xdg-open >/dev/null 2>&1; then
      xdg-open "${BASE_URL}${SWAGGER_PATH}"
    else
      echo "No platform opener found; printing URL instead: ${BASE_URL}${SWAGGER_PATH}"
    fi
    exit 0
  fi

  if [ $SECONDS -ge $deadline ]; then
    echo "Timed out after ${TIMEOUT}s waiting for ${BASE_URL}${API_DOCS_PATH}" >&2
    exit 2
  fi

  sleep ${SLEEP_INTERVAL}
done
