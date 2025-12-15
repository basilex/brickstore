DC := docker compose

.PHONY: help up up-dev down down-dev rebuild rebuild-dev logs logs-dev ps build build-dev shell-app shell-db exec-app open-swagger up-dev-open up-dev-open-bg

help:
	@echo "Usage: make <target>"
	@echo "Targets:"
	@echo "  up           Build and start production compose (docker-compose.yml)"
	@echo "  up-dev       Build and start development compose (docker-compose.dev.yml)"
	@echo "  down         Stop and remove production compose and volumes"
	@echo "  down-dev     Stop and remove development compose and volumes"
	@echo "  rebuild      Recreate production services (down + up)"
	@echo "  rebuild-dev  Recreate development services (down-dev + up-dev)"
	@echo "  logs         Follow logs for production compose"
	@echo "  logs-dev     Follow logs for development compose"
	@echo "  ps           List running compose services (uses production compose by default)"
	@echo "  shell-app    Open a shell into the 'app' container (dev compose recommended)"
	@echo "  shell-db     Open a psql shell into the 'db' container"
	@echo "  open-swagger Open the Swagger UI after the OpenAPI endpoint becomes available"
	@echo "  up-dev-open   Start dev stack and open Swagger UI when ready"
	@echo "  up-dev-open-bg Start dev stack and open Swagger UI in background (non-blocking)"

up:
	$(DC) -f docker-compose.yml up -d --build

up-dev:
	$(DC) -f docker-compose.dev.yml up -d --build

down:
	$(DC) -f docker-compose.yml down -v

down-dev:
	$(DC) -f docker-compose.dev.yml down -v

rebuild: down up

rebuild-dev: down-dev up-dev

logs:
	$(DC) -f docker-compose.yml logs -f --tail=200

logs-dev:
	$(DC) -f docker-compose.dev.yml logs -f --tail=200

ps:
	$(DC) -f docker-compose.yml ps

build:
	$(DC) -f docker-compose.yml build --no-cache

build-dev:
	$(DC) -f docker-compose.dev.yml build --no-cache

shell-app:
	$(DC) -f docker-compose.dev.yml exec app /bin/sh

shell-db:
	$(DC) -f docker-compose.dev.yml exec db psql -U postgres -d brickstore_dev

exec-app:
	# Run a command in the running app container. Usage: make exec-app CMD="./gradlew test"
	$(DC) -f docker-compose.dev.yml exec app sh -c "$(CMD)"

open-swagger:
	@./scripts/open-swagger.sh || (echo "Failed to open Swagger UI; ensure the app is running and reachable" >&2; exit 1)

up-dev-open:
	@$(MAKE) up-dev
	@$(MAKE) open-swagger

up-dev-open-bg:
	@$(MAKE) up-dev
	@$(MAKE) open-swagger &
