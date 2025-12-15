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

Notes
- The `dev` compose file mounts your project directory into the container and shares `~/.gradle` so incremental Gradle runs are faster. It runs `./gradlew bootRun` by default.
- Postgres data is stored in the named volume `db_data`. `make down` / `make down-dev` include `-v` to remove that volume; remove with care if you need to preserve data.
- The `Dockerfile` uses a multi-stage build producing a fat jar and then runs it with a slim JRE image.

If you want, I can also:
- Add `scripts/` helpers for macOS to open Swagger UI automatically after the stack is up.
- Add a `docker-compose.override.yml` for local overrides.
- Add a GitHub Actions workflow that builds the Docker image and runs tests inside Docker.
