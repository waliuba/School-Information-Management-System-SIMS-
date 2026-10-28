# ADR 0001: Phase 1 settings foundation

- **Status:** Accepted for Phase 1 implementation
- **Date:** 2026-10-26

## Context

SIMS has stateless JWT authentication, `ADMIN`/`TEACHER`/`STUDENT` roles, a profile email endpoint, a password change endpoint, Oracle-oriented JPA models, and no configured database migration runner. The requested Phase 1 needs protected settings, persisted account preferences, password invalidation, and an audit foundation.

## Decisions

1. **Authority:** existing `ADMIN` is the Phase 1 settings authority to preserve current admin account compatibility. Role assignment remains a separate concern; public self-registration now always creates `STUDENT`, regardless of the supplied role value. A dedicated `SUPER_ADMIN` role and bootstrap procedure remain a follow-up decision before Phase 2 access administration.
2. **Preference scope:** theme, sidebar collapsed state, and table page size are USER-scoped settings. Server values are account source of truth; localStorage retains last-known values for startup/offline display and is reconciled after the protected preference request.
3. **Persistence:** use a typed, allowlisted set of values in `SIMS_SETTING`, keyed by scope/owner/key with JPA optimistic versioning. Application defaults are follow-device theme, sidebar expanded, and 25 rows; absence of a row means default. Reset removes an override.
4. **Sessions:** put `TOKEN_VERSION` into each JWT and compare with the user record on authenticated requests. Password change increments it, invalidating all previously issued access tokens. The changed-password client signs out and prompts for login. No active-session listing is provided.
5. **Audit:** insert audit records in the same transaction as profile/password/preference updates. Include actor, action, target, UTC instant, and validated correlation ID. Omit credentials and email values. Oracle trigger prevents update/delete; retention and controlled archival must be designed before production operations.
6. **Migrations:** add a forward Oracle SQL script and a rollback script. No migration framework is configured, so these scripts are applied manually by the database owner after a backup. Automatic migration execution is not enabled. Confirm deployed Oracle version, current schema, DDL privileges, identity behavior, and recovery process before applying.
7. **Rate limiting:** password changes use a process-local 5-attempt/15-minute throttle per authenticated username and remote address. This is a Phase 1 baseline only; production multi-instance deployment must enforce a shared proxy/distributed limit.

## Consequences

- Existing `ADMIN` users can enter settings without a role migration. The label “Super Admin” is a product designation in this phase, not a separately persisted role.
- Adding `TOKEN_VERSION` requires applying the migration before new backend binaries serve authenticated requests. Rollback invalidates the revocation guarantee if the column is reset/dropped; rollback notes therefore require a recovery decision and backup.
- Audit events cannot be edited or deleted through normal SQL because of the trigger. Retention requires a separately reviewed archival process.
- Preference values can be cached on the current browser; server response replaces stale cached values after login.
- Process-local rate limiting is not sufficient for horizontally scaled deployments.
