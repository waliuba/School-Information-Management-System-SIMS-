# SIMS Super Admin Settings Module — Audit

**Audit scope:** repository inspection only. No application files were changed for the audit. This document is the requested output artifact. Builds and tests were not run.

**Evidence labels:** `[Verified: path]` means the cited repository source was inspected; `[Assumed]` is a proposal or design assumption; `[Unknown]` means the repository does not establish the fact. Line references are intentionally omitted so the evidence remains stable across edits.

## 1. Architecture summary

- [Verified: `README.md`, `front_end/package.json`, `front_end/vite.config.js`] The frontend is React with Vite, JavaScript/JSX, React Router, and Axios. The package uses Framer Motion and Lottie; it does not list a component or icon library. Package versions for several runtime dependencies are `latest`, so dependency resolution can drift.
- [Verified: `backend/pom.xml`, `backend/src/main/java/com/sims/backend/BackendApplication.java`] The backend is Spring Boot 4.1.0, Java 25, Spring MVC, Spring Security, Spring Data JPA, Bean Validation, Lombok, and JJWT. Oracle JDBC is runtime-scoped; H2 is used by test configuration.
- [Verified: `backend/src/main/resources/application.properties`, `README.md`] The configured production database dialect is Oracle. The README describes Oracle 21c XE. JPA schema generation defaults to `none`; the `DDL_AUTO` override is environment-configurable.
- [Verified: `compose.yaml`, `backend/Dockerfile`, `front_end/Dockerfile`] Compose defines backend and frontend services. The backend uses host networking and reads root `.env`; frontend serves a built Vite bundle through nginx. Compose does not define an Oracle service or health checks.
- [Verified: `front_end/src/services/api/axiosClient.js`, `front_end/src/context/AuthContext.jsx`, `front_end/src/services/storage/authStorage.js`] Axios attaches a bearer token read from browser localStorage. AuthProvider loads `/auth/me` on startup, stores token and user in localStorage, and clears local state on logout or a 401. There is no refresh-token request path in the inspected frontend.
- [Verified: `backend/src/main/java/com/sims/backend/config/SecurityConfig.java`, `backend/src/main/java/com/sims/backend/security/JwtService.java`, `backend/src/main/java/com/sims/backend/security/JwtAuthenticationFilter.java`] Backend sessions are stateless; JWTs include subject, issue time, and expiry. The filter reloads current user details and accepts a valid token while that account is enabled. No token version, refresh-token store, session registry, request rate limiter, or method-security annotation was found in the inspected backend source.
- [Verified: `backend/src/test/java/com/sims/backend/BackendApplicationTests.java`, `front_end/package.json`] The backend test tree contains a Spring context-load test and H2 test properties. The frontend package exposes dev/build/preview scripts only; no frontend test or lint script is configured.
- [Verified: `backend/src/main/java/com/sims/backend/config/DataSeeder.java`, `backend/src/main/resources/application.properties`] Startup seeding writes fixed demo accounts and school records and resets seeded account credentials/roles/enabled state on each run. This behavior must be considered before productionizing account controls.
- [Unknown: runtime infrastructure] Current container state, Oracle version actually used by deployment, database privileges, active configuration values, production browser support, and deployed security headers were not established by this source-only audit.

## 2. Design-system conventions to preserve

- [Verified: `front_end/src/styles/variables.css`] The UI uses CSS custom properties in `:root`: Inter/system font stack; type sizes from 0.75rem to 1.5rem; a 4px spacing base; neutral/blue semantic colors; 6/8/12px radii; two shadows; and a blue focus ring.
- [Verified: `front_end/src/styles/global.css`, `front_end/src/components/layout/MainLayout.jsx`, `front_end/src/components/layout/Sidebar.jsx`, `front_end/src/components/layout/Navbar.jsx`] Layout uses a fixed collapsible sidebar, a top navbar, and a page outlet. Mobile navigation is a drawer with a scrim; desktop content shifts based on sidebar width. Shared breakpoints include 767px and other page-specific breakpoints; the frontend guide recommends checking 360px, 768px, and 1280px.
- [Verified: `front_end/src/components/layout/PageContainer.jsx`, `front_end/src/components/common/{Button,Input,Card,Badge,Icon,Table,Pagination,Modal,ConfirmDialog,Tooltip}.jsx`, `front_end/src/context/ToastContext.jsx`] Reusable page container, button variants, labeled input/error wiring, cards, badges, inline SVG icon map, table, pagination, focus-managed modal/confirmation, tooltip, and toast context are present. Continue using these and avoid adding a competing framework/provider.
- [Verified: `front_end/src/styles/global.css`, `docs/FRONTEND_GUIDE.md`] CSS is regular class-based CSS; global styles import token, landing, and login styles. Reduced-motion rules exist. The existing guide records some text/focus color pairs below relevant WCAG contrast targets; new settings styles should use measured pairs and the smallest compatible token correction where needed.
- [Verified: `front_end/src/styles/global.css`, `front_end/src/pages/account/ProfilePage.jsx`, `front_end/src/pages/account/SecurityPage.jsx`] Account pages use `.account-card`, `.account-facts`, `.account-form`, `.account-notice`, and common button/input classes. Password form has client-side validation and field-specific accessible errors.
- [Verified: `front_end/src/components/common/Icon.jsx`] Icons are inline SVG path entries in the local `Icon` component; there is no external icon convention/library to add.
- [Unknown: typography scale beyond defined tokens] Page-level headings include 28px and custom values across styles; no formal type scale documentation beyond variables was found.
- [Unknown: rendered accessibility] Source-level labels/focus handling exist, but no rendered WCAG 2.2 AA audit, browser matrix, or automated axe result is available.

## 3. Reuse inventory

| Area | Existing reusable item | Audit finding |
|---|---|---|
| Shell/routing | [Verified: `front_end/src/routes/AppRoutes.jsx`, `front_end/src/components/layout/MainLayout.jsx`] Protected React Router routes share the current shell; admin-only routes use `RoleRoute`. | Existing role value is `ADMIN`; no distinct `SUPER_ADMIN` role is represented. |
| Account | [Verified: `front_end/src/pages/account/ProfilePage.jsx`, `front_end/src/pages/account/SecurityPage.jsx`, `front_end/src/pages/account/AccountSettingsPage.jsx`] Profile view/email edit; password change; basic account facts and logout. | Strong base for Account Essentials, but display fields currently derive from the stored user record. |
| Account API | [Verified: `front_end/src/services/api/accountApi.js`, `front_end/src/services/api/authApi.js`] `updateProfile`, `changePassword`, `getCurrentUser`, login and logout methods. | `getNotifications()` explicitly throws “not connected”; use no operational notification UI until an endpoint exists. |
| Backend profile API | [Verified: `backend/src/main/java/com/sims/backend/controllers/AuthController.java`, `backend/src/main/java/com/sims/backend/services/AuthService.java`, `backend/src/main/java/com/sims/backend/dtos/authentication/{UpdateProfileRequestDTO,ChangePasswordRequestDTO}.java`, `backend/src/main/java/com/sims/backend/dtos/user/UserResponseDTO.java`] `/api/auth/me` GET/PUT and `/api/auth/change-password` PUT. | Profile request only accepts email; password endpoint accepts current/new password. Extend only with additive, validated contracts. |
| User administration | [Verified: `backend/src/main/java/com/sims/backend/controllers/UserController.java`, `backend/src/main/java/com/sims/backend/services/UserService.java`, `backend/src/main/java/com/sims/backend/config/SecurityConfig.java`] Existing CRUD and an `ADMIN` request matcher. | `UserService` prevents self-update/self-delete and prevents changing or deleting the last enabled ADMIN. These checks cover ADMIN, not a distinct SUPER_ADMIN role, and need regression tests before extension. |
| UI primitives | [Verified: `front_end/src/components/common/`] Button, Input, Card, Badge, Icon, Table, Pagination, Modal, ConfirmDialog, Tooltip; `ToastContext`. | Reuse for shell/forms/statuses. Keep empty/loading/error semantics explicit. |
| Data mapping | [Verified: `backend/src/main/java/com/sims/backend/mappers/UserMapper.java`, `backend/src/main/java/com/sims/backend/dtos/`] Mapper and DTO conventions exist. | Account data should continue to use DTOs rather than exposing `UserModel` or password fields. |
| Errors | [Verified: `backend/src/main/java/com/sims/backend/exceptions/GlobalExceptionHandler.java`, `front_end/src/services/api/axiosClient.js`] Backend advice shapes errors; Axios normalizes status and message. | Worktree already contains an uncommitted edit to `GlobalExceptionHandler.java`; do not overwrite it during implementation. |
| Health/notifications/audit/backup | [Verified: `front_end/src/pages/dashboard/AdminDashboard.jsx`, `front_end/src/services/api/accountApi.js`; searched `backend/src/main`] Dashboard explicitly says health endpoints are not connected; notification API is a stub; no audit/backup/Actuator implementation found. | Do not present seeded labels, hardcoded status, or empty fabricated records as live operational data. |

## 4. Gap matrix — nine destinations

Legend: **Supported now** means an existing endpoint/behavior materially covers the destination; **Partial** means some reusable UI/API foundation exists; **Missing** means no operational implementation was found. Each cell includes its evidence.

| Destination | Status and evidence |
|---|---|
| Administrator Account | **Partial** — [Verified: `front_end/src/pages/account/ProfilePage.jsx`, `backend/src/main/java/com/sims/backend/controllers/AuthController.java`, `backend/src/main/java/com/sims/backend/dtos/authentication/UpdateProfileRequestDTO.java`] Current user can view account facts and edit email; profile request only models email. Password change exists separately. Additional administrator account fields are absent. |
| Security & Access | **Partial** — [Verified: `front_end/src/pages/account/SecurityPage.jsx`, `backend/src/main/java/com/sims/backend/config/SecurityConfig.java`, `backend/src/main/java/com/sims/backend/controllers/UserController.java`, `backend/src/main/java/com/sims/backend/services/UserService.java`, `backend/src/main/java/com/sims/backend/enums/Role.java`] Password change, user CRUD, coarse `ADMIN` route protection, self-change prevention and last enabled ADMIN protection exist. There is no separate super-admin permission model, failed-login history, session revocation, or visible evidence of method-level security. |
| School Configuration | **Missing** — [Verified: `backend/src/main/java/com/sims/backend/models/{ClassModel,Courses,AttendanceModel}.java`, searched `backend/src/main/java/com/sims/backend/controllers`] Academic domain entities exist, but no configuration aggregate/API for school identity, terms, grading scales, or attendance policy was found. |
| Notifications | **Missing** — [Verified: `front_end/src/services/api/accountApi.js`, `front_end/src/components/layout/Navbar.jsx`, `front_end/src/pages/shared/PlaceholderPage.jsx`] `getNotifications()` throws; navbar starts with a hardcoded empty array and displays “You’re all caught up.”; route is a placeholder. This is not evidence of delivery or persisted preferences. |
| Appearance & Preferences | **Missing** — [Verified: `front_end/src/styles/variables.css`, `front_end/src/components/layout/MainLayout.jsx`, searched `front_end/src`] Tokens and ephemeral sidebar state exist, but no theme mechanism/provider or persisted preference model was found. |
| Data Management & Backups | **Missing** — [Verified: `compose.yaml`, searched `backend/src/main`] No backup job, manifest/status source, export API, restore flow, or database backup integration was found. Compose declares no database service. |
| System Health & Information | **Missing** — [Verified: `front_end/src/pages/dashboard/AdminDashboard.jsx`, `backend/pom.xml`, searched `backend/src/main`] Dashboard explicitly reports endpoints are not connected. Actuator is not a dependency; no secured aggregation, timestamp, or health model was found. |
| Audit & Activity History | **Missing** — [Verified: `front_end/src/utils/activityLog.js`, searched `backend/src/main`] A frontend utility named activityLog exists, but no backend append-only audit model/service/API was found. The utility is not evidence of authoritative audit history. |
| Danger Zone | **Missing** — [Verified: `compose.yaml`, searched `backend/src/main/java/com/sims/backend/controllers`] No system-level destructive operation or recovery workflow was found. Do not build operational controls until each action has server enforcement, step-up verification, idempotency, audit and recovery design. |

## 5. Proposed navigation and component hierarchy

- [Assumed] Keep the existing shell and create a protected `/settings` entry point restricted to the designated super-admin permission once that permission exists on the server. Use nested routes so navigation and browser URLs identify the selected destination.
- [Assumed] Settings layout: `SettingsLayout` within `MainLayout` → settings title/intro → responsive settings navigation (nine links) → selected section outlet. On mobile, render a labeled selector or stacked navigation that does not compete with the global drawer.
- [Assumed] Section pages should be small feature components using the existing `PageContainer`, `Card`, `Input`, `Button`, `Badge`, `Table`, `Pagination`, `Modal`, `ConfirmDialog`, and toast/error patterns. Unbuilt destinations render an explicit “Planned for Phase X” state and must not contain pretend values or actionable controls.
- [Assumed] Proposed routes: `/settings/account`, `/settings/security`, `/settings/school`, `/settings/notifications`, `/settings/preferences`, `/settings/data`, `/settings/system`, `/settings/audit`, `/settings/danger`. Redirect `/settings` to account. Retain existing `/profile`, `/account-settings`, and `/security` paths as compatible redirects or aliases while replacing duplicated surfaces.
- [Assumed] Add the Settings entry to the navbar account menu and/or admin navigation only after the backend role gate is authoritative. Keep existing admin modules and student/teacher flows unchanged.

## 6. Proposed files to create or modify

All files below are proposals, not changes made in this audit.

### Frontend

- [Assumed] Modify `front_end/src/routes/AppRoutes.jsx`, `front_end/src/components/layout/Navbar.jsx`, and possibly `front_end/src/components/layout/Sidebar.jsx` to add guarded settings navigation and backward-compatible redirects.
- [Assumed] Create `front_end/src/pages/settings/SettingsLayout.jsx`, section pages or one section renderer, and a small section config module. Use explicit planned states for unsupported phases.
- [Assumed] Modify `front_end/src/services/api/accountApi.js` only for existing/new verified API contracts; create focused APIs for preferences, school settings, users/permissions, health, and audit as those backend phases are delivered.
- [Assumed] Add preference bootstrap logic near `front_end/src/main.jsx`/`AuthContext.jsx` only after deciding theme precedence and avoiding a pre-paint theme flash. Keep sidebar preference state distinct from user auth state unless a clear shared contract is needed.
- [Assumed] Extend `front_end/src/styles/variables.css` and `global.css` through existing tokens and account/settings classes. Record any contrast mismatch and the minimal compatible change.
- [Assumed] Add frontend tests and a11y tooling only with package changes justified by the existing absence of a test setup; establish focused coverage for routing, settings forms, validation, and preference bootstrap.

### Backend

- [Assumed] Modify `SecurityConfig.java` and add method security / authorization services; protect new account settings and management APIs on the server. Add security tests before exposing controls.
- [Assumed] Extend `UserModel` and account DTO/service/controller only for supported profile data, token-version/session invalidation, or preferences selected in the approved design. Do not return credentials or security secrets through DTOs.
- [Assumed] Create typed preference and school-configuration entities/services/DTOs/controllers in phases; use optimistic versioning and validation for mutable settings.
- [Assumed] Create append-only audit entity, repository, service, and narrowly scoped viewer endpoint. Audit the setting changes in the same transaction; redact secrets and write actor, UTC timestamp, correlation ID, and sanitized before/after values.
- [Assumed] Add health integration only after Actuator/deployment topology is known; add backup status/export only after a verifiable job/manifest source and safe execution limits exist.
- [Assumed] Add MockMvc/security/service/repository tests. Preserve the pre-existing uncommitted `GlobalExceptionHandler.java` edit and review its diff before touching that file.

## 7. Proposed DB/config changes and migration approach

- [Verified: `backend/src/main/resources/application.properties`, `backend/pom.xml`, `backend/attendance-migration.sql`, `backend/attendance-status-migration.sql`, `backend/department-course-migration.sql`] JPA DDL defaults to `none`; there is no Flyway or Liquibase dependency/configuration. Existing SQL migrations are standalone files. Oracle identity mappings occur in several models, but no common migration runner or rollback convention is evident.
- [Assumed] Prefer introducing a single migration tool before settings schema changes. Given SQL already exists, Flyway is a reasonable proposal, but first confirm Spring Boot 4.1 support, Oracle edition/version, schema owner privileges, baseline procedure, and compatibility with existing manually applied scripts. Do not silently adopt a migration tool and auto-run it in production.
- [Assumed] Use additive expand/contract migrations: add new tables/nullable columns/indexes first; deploy code that can read old and new state; backfill if needed; only later enforce constraints or remove obsolete storage. Every forward script needs a tested rollback/recovery note. Avoid destructive rollback assumptions for changed data.
- [Assumed] Proposed typed tables: `SIMS_USER_PREFERENCE` keyed to user; `SIMS_SCHOOL_SETTING` for scoped school settings; `SIMS_SYSTEM_SETTING` only for application-level values intentionally persisted in DB; `SIMS_AUDIT_EVENT` append-only. Typed tables fit the small, security-sensitive setting domain better than an unvalidated generic key/value bag. If arbitrary keys are later required, include type/schema version, scope, validation and optimistic version columns.
- [Assumed] Store scope explicitly as USER, SCHOOL, or SYSTEM. Store stable defaults in application code/schema contract, not fictitious database rows. Reset means deleting the user override or restoring a documented school default; it must not reset unrelated settings. Include `VERSION` for optimistic concurrency and invalidate caches only after committed updates.
- [Unknown] Oracle identity support and current identity/sequence ownership on the deployed database, reserved-word conflicts for proposed names, migration account DDL privileges, schema baseline, and actual rollback process are not verified.

## 8. Cross-cutting decisions: options and recommendations

### 8.1 Setting scopes and storage

- [Assumed] Options: (A) typed per-domain tables; (B) generic key/value table with JSON/string payload. Recommend typed tables for preferences, school configuration and audit because fields are known and need database constraints. Reserve key/value for genuinely dynamic ops configuration with schema/type validation. Keep USER, SCHOOL and SYSTEM explicit and authorize by scope.
- [Assumed] Use stable documented defaults; reset removes the override or restores the scope default. Add `VERSION` and compare-and-set updates to prevent lost updates. Cache only after need is measured; invalidate on committed write and across nodes if deployment becomes multi-instance.

### 8.2 Authorization

- [Verified: `backend/src/main/java/com/sims/backend/enums/Role.java`, `backend/src/main/java/com/sims/backend/config/SecurityConfig.java`, `backend/src/main/java/com/sims/backend/controllers/AuthController.java`] Current roles are ADMIN, TEACHER, STUDENT; URL rules use `hasRole("ADMIN")`; registration is permitted without authentication and request DTO includes a role. This is a privilege-assignment surface that requires immediate review before adding any super-admin UI.
- [Assumed] Options: (A) add a distinct `SUPER_ADMIN` role; (B) add granular permissions to existing roles. Recommend a distinct, server-authorized `SUPER_ADMIN` authority for this module initially, then migrate to capabilities only if finer delegation is required. A client route guard is convenience only; backend authorization is authoritative.
- [Assumed] Add method-level checks for sensitive service/controller operations, restrict who can assign privileged roles, protect built-in/system admin accounts, prevent demoting/deactivating/deleting the last active super-admin, and require explicit policy for self-demotion. Cover 401, 403, role escalation, protected account, last-admin and self-demotion tests.
- [Unknown] Existing role/account data quality and whether the deployment has a bootstrap procedure for creating the first super-admin are unknown.

### 8.3 Sessions and password changes

- [Verified: `backend/src/main/java/com/sims/backend/config/SecurityConfig.java`, `backend/src/main/java/com/sims/backend/controllers/AuthController.java`, `backend/src/main/java/com/sims/backend/services/AuthService.java`, `front_end/src/services/storage/authStorage.js`] JWTs are stateless and live until expiry; logout endpoint returns success without revoking token; browser stores the bearer token in localStorage. Existing account UI correctly notes sessions are not available from current authentication data.
- [Assumed] Options: (A) keep short-lived access JWTs with server-side refresh-token/session store; (B) add a user `tokenVersion` claim checked against DB on every request. Recommend refresh-token/session storage if “sign out other sessions” is required; otherwise omit active sessions. For immediate password-change invalidation, add token version and increment on password/privilege changes, or move to persisted sessions. Revise JWT filter to enforce the selected version/revocation contract.
- [Assumed] Password change must verify current password, audit success without secrets, apply a configurable rate limit, and invalidate previous tokens. Define whether the current browser must reauthenticate after success and ensure the UX follows the backend contract.

### 8.4 Audit foundation

- [Verified: searched `backend/src/main/java/com/sims/backend`] No authoritative audit service/model or audit API was found.
- [Assumed] Implement append-only audit writes in the same transaction as settings mutation, with actor ID, UTC instant, correlation/request ID, action, target, and redacted before/after diff. Never record passwords, bearer/refresh tokens, secret values, or unnecessary student data. Enforce no-update/no-delete at application and DB privilege/trigger policy where feasible; define retention and privileged export access. Tests should verify same-transaction behavior, redaction, pagination and integrity constraints.

### 8.5 Migrations

- [Verified: `backend/pom.xml`, `backend/src/main/resources/application.properties`, `backend/*.sql`] No migration framework is configured; handwritten scripts exist; Hibernate schema auto-update is not the default.
- [Assumed] Adopt a controlled migration runner with baseline and deployment sequencing. Verify exact Oracle version, reserved words, identity/sequence convention, schema privileges and backup/recovery before migrations. Provide forward script and documented rollback for each migration; test against Oracle (e.g., Testcontainers if compatible/available) rather than relying only on H2.

### 8.6 Preference precedence and theme startup

- [Verified: `front_end/src/styles/variables.css`, `front_end/src/main.jsx`, searched `front_end/src`] There is a single light token set and no detected theme bootstrap/provider. Sidebar collapse is component state and resets on reload.
- [Assumed] Options: (A) server profile always wins; (B) device-local theme wins over server theme. Recommend USER setting as account source of truth for cross-device theme and sidebar/page-size preferences; allow a device-local override only if explicitly presented as device-specific. Before React first paint, resolve saved preference and `prefers-color-scheme` using a tiny prepaint script/class attribute to avoid flash. When offline, use cached last-known preference and reconcile on next successful load using version/timestamp.

### 8.7 Uploads (logo/avatar)

- [Unknown] No avatar or school-logo upload endpoint, storage provider or file pipeline was found.
- [Assumed] Defer uploads from Phase 1. If later implemented, define byte and pixel limits, extension allowlist plus content sniffing, re-encode raster formats, malware scanning where available, generated storage keys, authorization, and path-traversal-safe object storage. Never trust client MIME type or store arbitrary paths.

### 8.8 Non-functionals

- [Verified: `docs/FRONTEND_GUIDE.md`, `backend/src/main/java/com/sims/backend/controllers`, searched project] The frontend guide documents keyboard and contrast conventions; backend list endpoints examined return unpaged lists; no rate-limiter or formal browser support policy was found.
- [Assumed] Target WCAG 2.2 AA; define supported browsers before release; persist timestamps in UTC and display in the administrator’s chosen timezone; rate-limit login/password/step-up endpoints; review CSRF/XSS/CORS/security headers with the actual token-storage model; paginate all new list APIs server-side; sanitize operational health details. Treat current localStorage token storage and broad CORS settings as items for security review, not as a setting feature to copy blindly.
- [Unknown] Production TLS termination, CSP/security headers, CORS origins, proxy/request-ID behavior, time-zone policy and deployment rate limiting were not established.

## 9. Risks and mitigations

| Risk | Evidence and mitigation |
|---|---|
| Privilege escalation through signup/role assignment | [Verified: `backend/src/main/java/com/sims/backend/config/SecurityConfig.java`, `backend/src/main/java/com/sims/backend/controllers/AuthController.java`, `backend/src/main/java/com/sims/backend/dtos/authentication/RegisterRequestDTO.java`] Registration is public and accepts a role field. Remove public privilege choice or constrain signup to a safe role/invitation flow; add escalation tests before super-admin settings. |
| Password changes do not revoke issued JWTs | [Verified: `backend/src/main/java/com/sims/backend/services/AuthService.java`, `backend/src/main/java/com/sims/backend/security/JwtService.java`, `backend/src/main/java/com/sims/backend/security/JwtAuthenticationFilter.java`] Existing JWT validity checks expiry and subject, not password/version. Add revocation/version or persisted sessions and test existing-token rejection. |
| Seed process resets privileged credentials | [Verified: `backend/src/main/java/com/sims/backend/config/DataSeeder.java`] Seeded accounts are reset on application startup. Gate/remove production seeding and separate dev/test seed profiles before account management is relied upon. |
| Destructive user CRUD can remove a privileged account | [Verified: `backend/src/main/java/com/sims/backend/controllers/UserController.java`, `backend/src/main/java/com/sims/backend/services/UserService.java`] Self-update/self-delete and last enabled ADMIN guards exist. Preserve and test them; extend the policy to the approved super-admin role and consider transaction/locking behavior for concurrent last-admin operations. |
| Fake status/notification UX | [Verified: `front_end/src/components/layout/Navbar.jsx`, `front_end/src/pages/dashboard/AdminDashboard.jsx`, `front_end/src/services/api/accountApi.js`] Navbar invents empty notification state and dashboard displays static health labels. Replace with explicit not-connected/planned states until backed by real APIs. |
| Migration drift and Oracle mismatch | [Verified: `backend/pom.xml`, `backend/src/main/resources/application.properties`, `backend/*.sql`] No migration runner, dialect is Oracle, tests use H2. Establish baseline, Oracle-specific migration/recovery review and migration tests before adding tables. |
| Deployment/build incompatibility | [Verified: `backend/Dockerfile`, `front_end/Dockerfile`, `compose.yaml`] Backend Docker build skips tests; frontend Dockerfile has a `npm ci` line with unusual trailing `i` in fetch timeout and Compose does not configure health checks. Verify existing container builds before changing deployment files; do not bundle unrelated cleanup into settings work. |
| Unreviewed worktree change | [Verified: `git status --short`] `backend/src/main/java/com/sims/backend/exceptions/GlobalExceptionHandler.java` is already modified. Preserve it and inspect its diff before any future edits to the same file. |
| Browser storage token exposure | [Verified: `front_end/src/services/storage/authStorage.js`, `front_end/src/services/api/axiosClient.js`] Bearer token is in localStorage, exposed to same-origin script execution. Review XSS defenses and token storage/session architecture before adding settings that make claims about session security. |

## 10. Phased plan and acceptance criteria

### Phase 0 — approval and security/migration groundwork

- [Assumed] Acceptance: written approval for the proposed scope/decisions; agree super-admin bootstrap and role semantics; inspect the existing uncommitted exception-handler diff; select migration/baseline procedure; no new operational UI shown as live.
- [Assumed] Tests: add focused authorization regression tests for public registration role assignment, 401/403, and privileged account protections before settings routes are granted.

### Phase 1 — Foundation + Account Essentials

- [Assumed] Deliver responsive settings shell with all nine destinations; every unbuilt destination displays “Planned for Phase X.” Establish server-side authorization primitives, typed setting store as approved, audit service, and migration infrastructure. Administrator Account edits only fields supported by reviewed DTOs and uses the existing password endpoint after audited/token-invalidation design. Preferences cover light/dark/system, sidebar behavior and default page size with agreed precedence.
- [Assumed] Acceptance: no fake operational data; all forms have loading/empty/success/error states; server validation and accessible errors; old URLs remain compatible; password change invalidates old tokens; audit records changes without secrets; design tokens and responsive shell are reused; migration forward/recovery instructions documented.
- [Assumed] Tests: frontend routing/role guard, form validation and preference bootstrap; backend service/MockMvc tests for profile/password, 401/403, token invalidation, audit atomicity/redaction, optimistic conflict, and migration forward/rollback. Run existing suites and show actual command output.

### Phase 2A — School Administration

- [Assumed] Deliver school profile, academic years/terms, grading scales and attendance rules with explicit SCHOOL scope, defaults/reset behavior, server validation, optimistic locking, and same-transaction audit.
- [Assumed] Acceptance: existing academic CRUD/API contracts unchanged; concurrent updates return a clear conflict; defaults and reset are documented; Oracle migration tested both forward and recovery path.
- [Assumed] Tests: validation boundaries, permission isolation, reset behavior, concurrent version conflict, repository/migration tests against Oracle-compatible test environment, existing CRUD regression.

### Phase 2B — Access, Notifications, Audit UI

- [Assumed] Deliver user/role/permission administration, activation/deactivation, failed-login history only if collected safely, notification preference/policy storage only, and a read-only paginated audit viewer.
- [Assumed] Acceptance: no notification delivery is described as operational unless a delivery backend exists; protected and last-super-admin rules enforced on server; audit viewer is paginated and redacts secrets; list endpoints do not return unbounded rows.
- [Assumed] Tests: privilege escalation/self-demotion/protected account, unauthorized mutation, pagination/filter boundaries, audit read access/redaction, and notification policy persistence without delivery claims.

### Phase 3 — Operational controls

- [Assumed] Health only if deployment supports secured Actuator aggregation for API and Oracle. Include last-checked time and healthy/degraded/down/stale/unknown states; sanitize internals. Backups only from verifiable job/manifest source, with authorized async export limits and audit; no restore in UI. Danger Zone actions only after consequence copy, step-up re-authentication, typed confirmation, idempotency, server authorization, audit, no hard-delete, and documented recovery path.
- [Assumed] Acceptance: each status is sourced and timestamped; stale/unavailable states cannot look healthy; exports are bounded and logged; every system action has a recovery runbook and double-submit protection.
- [Assumed] Tests: component health degradation/staleness, unauthorized export/action, output sanitization, idempotency, step-up expiry, audit emission and recovery procedure review.

## 11. Open questions for the owner

1. [Unknown] Should `SUPER_ADMIN` be a new distinct role, or should the existing `ADMIN` role become the authority for the settings module? Who is allowed to bootstrap the first privileged account?
2. [Unknown] Which administrator profile fields are required beyond the current username/email, and should those be stored on `UserModel` or a separate profile record?
3. [Unknown] Is cross-device theme/sidebar/page-size persistence required, or should any preference remain device-local?
4. [Unknown] Which Oracle version/edition and deployment DB user privileges are used in the target environment, and are existing SQL scripts already applied there?
5. [Unknown] Is the current seeder restricted to local development? It rewrites fixed account credentials and roles on every startup.
6. [Unknown] Which production deployment provides health/backup sources, and is Actuator or a backup job/manifest already available outside this repository?
7. [Unknown] What browser support policy, retention period for audit records, and timezone display policy should the module follow?

## Audit stop

This document completes the audit deliverable. No settings module implementation has begun. The brief requests a hard stop until written approval; implementation should start only after that approval and resolution of the security, migration, and preference decisions above.
