# Contributing to Brickstore

Thanks for your interest in contributing! This document explains how to propose changes, format commits, and open pull requests so your contribution can be reviewed and merged quickly.

## Table of contents

- How to contribute
- Branching strategy
- Commit message format
- Pull request (PR) guidelines
- PR checklist
- Running tests and linters
- Code review and merging
- Reporting issues

## How to contribute

1. Fork the repository (if you don't have push access) or create a branch from `dev` in this repository.
2. Implement your change on a descriptive branch (see Branching strategy below).
3. Run tests and ensure the build is green locally.
4. Open a Pull Request against `dev` with a clear description of the change.

## Branching strategy

- Base branches: work off `dev` (the main development branch).
- Feature branches: prefix with `feature/` for new features, `fix/` for bug fixes, `chore/` for maintenance, `docs/` for documentation, and `test/` for tests.
- Example branch names:
  - `feature/add-user-endpoint`
  - `fix/currency-conversion-bug`
  - `chore/error-handling-cleanup`

Keep branches small and focused; one logical change per branch makes review and bisecting easier.

## Commit message format

We use a Conventional-style commit guideline. Format:

```
<type>(<scope>): <short summary>

<optional body>

<optional footer>
```

- `type` should be one of: `feat`, `fix`, `docs`, `chore`, `refactor`, `test`, `ci`, `build`.
- `scope` is optional but recommended (e.g., `api`, `db`, `service`, `docs`).
- Keep the subject line <= 72 characters. Use the body to explain the what and why (not the how).

Examples:

```
feat(api): add endpoint to list currencies

Adds a new GET /api/currencies endpoint and integration tests.

Fixes: #42
```

```
fix(user-service): return 404 when user not found

Improves error handling and maps domain ErrorCode to RFC7807 Problem `type`.
```

If a change requires multiple commits, prefer squashing or rebasing into a single logical commit before merging.

## Pull request (PR) guidelines

- Target branch: set the PR base to `dev`.
- Title: start with the commit `type(scope): short summary` where possible.
- Description: include a short summary, motivation, design decisions, and any migration notes. If the PR fixes an issue, reference it (`Fixes #NN`).
- Attach screenshots or API responses if the change affects the API surface.
- Tests: add or update unit/integration tests for your change.

## PR checklist

Before requesting a review, ensure the PR meets the following:

- [ ] Code builds locally: `./gradlew clean build` (or at least `./gradlew clean test`).
- [ ] All tests pass.
- [ ] New behavior is covered by tests (unit or integration) where applicable.
- [ ] Public API changes documented (README, `docs/API_ERRORS.md`, or OpenAPI annotations when relevant).
- [ ] Commit history is reasonable (squash/sort commits if necessary).
- [ ] Update `CHANGELOG.md` when making user-facing changes.

## Running tests and linters

- Run the full test suite locally:

```bash
./gradlew clean test
```

- If you alter code style or formatting, run the project's formatter (if configured) or your IDE formatter to keep style consistent. If the repository adopts a tool like Spotless or Checkstyle, run it as part of your build.

## Code review and merging

- At least one approving review is required before merging. The PR author should address review comments and push changes to the same branch.
- CI must pass on the PR before merging.
- Squash and merge is the preferred merge strategy for keeping `dev` history concise, unless reviewers request preserving commits.

## Reporting issues

Open issues for bugs or feature requests in GitHub. A good issue includes steps to reproduce, expected and actual behavior, and any relevant logs or stack traces.

## Contact

If you're unsure about where to start or need guidance on architecture, open a draft PR or start a discussion in the issue tracker.

Thank you for contributing to Brickstore!
