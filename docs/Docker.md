**Docker: Development and Production (quick guide)**

This project provides two docker-compose files at the repository root:

- `docker-compose.yml` — production-ish composition: a Postgres service and the app built into a container from the `Dockerfile`.
- `docker-compose.dev.yml` — development composition: mounts the project into the container so you can run `./gradlew bootRun` inside the container and iterate quickly.

Prerequisites

- Docker & Docker Compose plugin (Docker Desktop or equivalent)

Common workflows (from repository root)

Start the production stack (build image and start detached):

```
make up
```

Start the development stack (mount sources, use `bootRun`):

```
make up-dev
```

Follow logs

```
make logs      # production compose
make logs-dev  # development compose
```

Stop and remove containers + named volumes

```
make down
make down-dev
```

Recreate services

```
make rebuild
make rebuild-dev
```

Open a shell inside the running `app` container (dev compose recommended):

```
make shell-app
```

Open a psql shell into the `db` container (dev compose):

```
make shell-db
```

Open the Swagger UI in your default browser after the stack is up (waits for `/v3/api-docs` to be available):

```
make open-swagger
```

Non-blocking helper

- There's also a non-blocking variant `make up-dev-open-bg` which starts the dev stack and launches the `open-swagger` script in the background. This returns control to your shell immediately while the script polls the OpenAPI endpoint and opens Swagger when it's ready.

Usage examples

```
make up-dev-open       # starts dev stack, opens Swagger and waits until Swagger is opened
make up-dev-open-bg    # starts dev stack and opens Swagger in background (returns immediately)
```

Print current defaults

You can print the Makefile defaults for the Swagger helper with:

```
make show-defaults
```

Example output:

```
OPEN_SWAGGER_URL=http://localhost:8081
OPEN_SWAGGER_TIMEOUT=60
```

Notes

- The project exposes OpenAPI at `/v3/api-docs` and the Swagger UI at `/swagger-ui/index.html` when the app is running inside the compose stack.
- `make open-swagger` runs `scripts/open-swagger.sh` which polls the OpenAPI endpoint and opens the Swagger UI in your default browser when available. Defaults: `http://localhost:8081` and a 60s timeout.
- You can run the script directly with a custom base URL or timeout:

```
./scripts/open-swagger.sh http://localhost:8081 120
```

Changing the host port

- If your machine already uses host port `8081`, you can either stop that process or change the host-side mapping for the `app` service in `docker-compose.dev.yml` (host:container). Example change to use `8082` on the host:

```yaml
services:
	app:
		ports:
			- "8082:8081"
```

- Alternatively, make the `docker-compose` port configurable by using an env var in the compose file and export `HOST_PORT` before `make up-dev`.

Notes

- The `dev` compose file mounts your project directory into the container and shares `~/.gradle` so incremental Gradle runs are faster. It runs `./gradlew bootRun` by default.
- Postgres data is stored in the named volume `db_data`. `make down` / `make down-dev` include `-v` to remove that volume; remove with care if you need to preserve data.
- The `Dockerfile` uses a multi-stage build producing a fat jar and then runs it with a slim JRE image.

If you want, I can also:

- Add `scripts/` helpers for macOS to open Swagger UI automatically after the stack is up.
- Add a `docker-compose.override.yml` for local overrides.
- Add a GitHub Actions workflow that builds the Docker image and runs tests inside Docker.
