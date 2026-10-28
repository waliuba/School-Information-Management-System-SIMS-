# Phase 1 Report — Settings Foundation and Account Essentials

**Branch:** `phase-1-settings`  
**Status:** implementation delivered; Oracle migration verification remains a release acceptance gate. Hard stop before Phase 2.

## Summary

- Added a responsive settings surface with all nine destinations. Account, Security, and Appearance & Preferences have Phase 1 content; later destinations show an explicit planned phase.
- Connected preferences to a protected API and USER-scoped Oracle table. Theme, collapsed sidebar, and table page size defaults are persisted; reset removes account overrides. The browser keeps last-known preferences for startup and offline display.
- Reused existing profile email and password endpoints. Password changes are rate-limited, audited, increment `TOKEN_VERSION`, invalidate prior JWTs, and end the current browser session.
- Added same-transaction audit writes for profile email, password, and preference changes. Added a database trigger to reject audit event update/delete.
- Public registration now assigns `STUDENT` even if a request supplies another role. Settings authorization is enforced through the backend route matcher and method security; the existing `ADMIN` role is the Phase 1 settings authority.
- Replaced the navbar’s fabricated empty notification feed with a clear Phase 2B notice.

## Files changed

- Frontend routing, shell navigation, account security flow, preference API/cache, theme variables, page-size defaults, responsive settings page and styles.
- Backend security config, auth/profile/password services and controller, JWT claim/filter, token version field, rate limiter, settings and audit entities/repositories/services/controller, and optimistic-lock conflict response.
- Oracle forward and rollback SQL under `backend/db/migration/`.
- Added backend unit tests and a Mockito test mock-maker setting for the Java 25 environment.
- Added ADR 0001 and retained the source-only audit at `docs/super-admin-settings-audit.md`.
- `GlobalExceptionHandler.java` was already modified in the worktree before Phase 1. That existing logger change was preserved; the optimistic-lock conflict handler was added to the same file after reviewing its diff.

## API and schema changes

- Existing API contracts retained for `/api/auth/me` and `/api/auth/change-password`.
- Added `GET` and `PUT /api/settings/me/preferences`, returning/accepting `theme`, `sidebarCollapsed`, and `tablePageSize` for the authenticated user’s own record. The settings UI routes remain restricted to `ADMIN`.
- Registration request shape is unchanged; the backend no longer honors its submitted role and creates a `STUDENT` account.
- Password JWTs now contain `ver`; the request filter checks it against `USERS.TOKEN_VERSION`.
- `backend/db/migration/V1__settings_foundation.sql` adds `USERS.TOKEN_VERSION`, `SIMS_SETTING`, `SIMS_AUDIT_EVENT`, audit indexes and an append-only trigger. The rollback is documented at `backend/db/migration/rollback/V1__settings_foundation.sql`.
- No migration framework exists. The scripts are manual and were not applied to Oracle in this phase.

## Tests run and results

- `front_end`: `npm run build` — **passed**. Vite reported the existing Lottie `eval` warning and a JavaScript chunk larger than 500 kB.
- `backend`: `./mvnw -q test` — **passed**, all 5 tests: the Spring context test and 4 focused tests covering public role containment, token-version update/audit, JWT version claims, and rate limiting.
- `backend`: the focused command `./mvnw -q -Dtest=AuthServiceSettingsTest,PasswordChangeRateLimiterTest,JwtServiceTokenVersionTest test` also passed independently (4 tests).
- `git -c core.whitespace=cr-at-eol diff --check` — **passed**. Existing frontend files use CRLF line endings.
- Oracle migration forward/rollback, browser interaction, accessibility, API authorization with HTTP 401/403, and E2E flows were not run.

## Security notes

- Existing `ADMIN` accounts are the initial settings authority; there is not yet a distinct `SUPER_ADMIN` role or bootstrap flow.
- Password change invalidates all older access tokens. The in-memory limiter is per process and does not provide a shared limit across multiple backend instances.
- Audit values omit email addresses and credential material. The Oracle trigger prevents normal update/delete statements, but the schema owner can disable it; retention/archival remains to be designed.
- Apply the schema migration before deploying the updated backend because the JPA user model now reads `TOKEN_VERSION`.

## Known limitations

- No separate SUPER_ADMIN role, active-session list, sign-out-other-sessions, audit viewer, notification delivery, health integration, backup status/export, school configuration, or Danger Zone action was introduced.
- Settings API/audit behavior is covered by unit/context tests but not by MockMvc authorization integration tests.
- Oracle DDL and rollback have not been exercised against the target Oracle version. The migration requires a backup, schema-owner review, and confirmation of identity/DDL privileges before manual application.
- The configured database migration process, production CSP/CORS/headers, browser support matrix, full WCAG 2.2 AA check, and distributed rate limiting remain deployment work.
- Frontend package has no test framework; frontend verification was a production build only.
- The frontend build still emits the existing Lottie `eval` warning and large-bundle warning.

## Rollback instructions

1. Stop or route traffic away from the updated backend before schema changes.
2. Restore the previous backend/frontend artifacts. Existing endpoints remain compatible, but the new frontend settings API requires the new backend and schema.
3. Before executing the rollback SQL, export and retain audit records required by policy. The rollback drops settings and audit tables; it is data destructive. Dropping `TOKEN_VERSION` also removes the JWT invalidation state. Confirm the desired session-revocation behavior before proceeding.
4. Apply `backend/db/migration/rollback/V1__settings_foundation.sql` only under an approved Oracle recovery plan. This script was not executed or tested against Oracle in this phase.

## Proposed next-phase adjustments

- Before Phase 2A/2B, decide whether to introduce a distinct `SUPER_ADMIN` role and define first-account bootstrap. Add MockMvc tests for 401/403, role escalation, last-admin and protected-account behavior.
- Establish and baseline a migration framework or document a durable manual migration runbook; validate Oracle version and permissions and run forward/recovery tests against Oracle-compatible infrastructure.
- Add the read-only paginated audit viewer and user/role controls only after privilege safeguards and database concurrency behavior are tested.
