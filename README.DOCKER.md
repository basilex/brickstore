Running Brickstore in Docker

Quick start (build jar and run):

1. Build and start Postgres + app (multi-stage build)

```bash
docker compose up --build
```

- App will be available on `http://localhost:8081`.
- Postgres data is persisted in Docker volume `db_data`.

Development mode (hot-reload with `bootRun`):

1. Start using the dev compose file (this mounts your source into the container and runs `./gradlew bootRun`):

```bash
docker compose -f docker-compose.yml -f docker-compose.dev.yml up --build
```

Notes & environment:

- The compose files set Spring Boot properties via environment variables (e.g. `SPRING_DATASOURCE_URL`). These map to `spring.datasource.url`.
- If you want the app to use a different Spring profile, set `SPRING_PROFILES_ACTIVE` accordingly.

Run tests inside container (optional):

```bash
# build image only (no run)
docker compose build app

# run gradle tests in a temporary container
docker run --rm -v "$PWD":/home/gradle/project -w /home/gradle/project gradle:8.6-jdk17 ./gradlew test --no-daemon
```

If you prefer not to build in Docker, run locally with your JDK and Gradle wrapper:

```bash
./gradlew clean bootRun
```
