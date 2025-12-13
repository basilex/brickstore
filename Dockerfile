# Multi-stage build: build the Spring Boot fat jar with Gradle, then run on a slim JRE
FROM gradle:8.6-jdk21 AS builder
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .
RUN gradle clean bootJar --no-daemon -x test

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /home/gradle/project/build/libs/*.jar /app/app.jar

# Create non-root user for running the app
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser && \
	chown -R appuser:appgroup /app

EXPOSE 8081
USER appuser
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
