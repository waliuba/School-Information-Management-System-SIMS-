# SIMS Frontend Guide

This guide describes the current React frontend in `front_end/`. It is intended as a short map for maintaining the project and explaining its UI choices.

## Start and build

From `front_end/`:

```sh
npm run dev
npm run build
```

The frontend uses JavaScript, React, Vite, React Router, Axios, and the dependencies already listed in `front_end/package.json`. It does not use TypeScript or a separate component library. The current package scripts do not include lint or test commands.

## Application structure

- `src/main.jsx` starts the React application and loads global styles.
- `src/App.jsx` provides the shared toast context and renders `AppRoutes`.
- `src/routes/AppRoutes.jsx` declares public routes, protected routes, and role-limited routes.
- `src/routes/ProtectedRoute.jsx` waits for session loading and redirects signed-out users to login.
- `src/routes/RoleRoute.jsx` limits selected pages by role. Backend security remains authoritative.
- `src/components/layout/MainLayout.jsx` combines the responsive sidebar, top navigation, and route outlet.
- `src/components/layout/PageContainer.jsx` provides the shared page title, description, and action area.
- `src/pages/` contains dashboards, account pages, and module pages. `CrudModulePage.jsx` reuses CRUD behavior for students, courses, enrollments, and teachers where routed.
- `src/services/api/` contains the Axios client and API methods. Keep endpoint paths and request/response shapes aligned with backend controllers and DTOs.

## Styling and tokens

Global styles are in `src/styles/global.css`; shared CSS custom properties are in `src/styles/variables.css`. The login and landing pages also have dedicated stylesheets imported by the global stylesheet. Components use regular class names and shared tokens rather than CSS modules.

The token file defines the platform font stack, type sizes, spacing, radii, shadows, and semantic colors. Spacing uses a 4px base. Existing SIMS blues and neutral surfaces form the main palette; use the semantic success, warning, danger, and info colors for status feedback. Add or reuse tokens instead of introducing one-off palette values when practical.

The shell has a collapsible sidebar on larger screens and a mobile navigation drawer. The table wrapper owns horizontal overflow, keeping wide data tables inside their content region. Shared responsive rules are in `global.css`; check the actual page at 360px, 768px, and 1280px when changing layout.

## Shared components

| Component | Use |
| --- | --- |
| `Button` | Button variants using the shared button classes. |
| `Input` | Labeled input with `aria-invalid` and linked error text when an error is supplied. |
| `Card` | Surface container with optional semantic element and class name. |
| `Badge` | Compact status label with neutral, success, warning, danger, or info tone. |
| `Icon` | Small inline SVG from the project's existing icon paths. Supply a label only when the icon conveys information by itself. |
| `Avatar` | Image or first initial, hidden from assistive technology when adjacent identity text is present. |
| `Table` | Data table with loading and empty states; wide tables scroll inside `.table-wrap`. |
| `Pagination` | Page controls, page size, and an accurate range/count supplied by the list page. |
| `StudentPerformanceTable` | Dashboard performance table with client-side pagination shared by Admin and Teacher dashboards. |
| `Modal` | Dialog with initial focus, Escape handling, Tab focus containment, and focus restoration. |
| `ConfirmDialog` | Modal confirmation with busy state and optional error display. |
| `Tooltip` | Short supplemental text attached to one interactive child. |
| `useToast` | Shared, temporary status notifications. Keep messages free of student names and identifiers. |

Prefer these shared pieces over copying markup into another page. Keep API and permission behavior in the page/service layer rather than treating hidden controls as security.

## Data and list behavior

Inspect the backend controller and DTO before changing a list. Do not assume pagination, filter names, enum values, or response envelopes. `resourcesApi.js` normalizes current list responses; preserve the endpoint contract when adding UI behavior.

The Students endpoint currently returns a full list. Its list controls filter, sort, and paginate in the browser; student search remains in component state to avoid putting student names in the URL. Enrollment, examination results, and dashboard performance lists also use client-side pagination over their returned rows. The results print view retains the full result set regardless of the visible page. Teachers list state is stored in query parameters. Do not silently display an empty state after a failed request, or report a failed mutation as success.

## Accessibility and motion

- Associate every input with a visible label; connect field errors with `aria-describedby` and announce errors where appropriate.
- Preserve keyboard focus indicators. Dialogs must keep focus inside, close on Escape where allowed, and return focus to the trigger.
- Use semantic table headers and accessible names for icon-only controls.
- Keep mobile controls comfortably operable and prevent page-level horizontal overflow; let wide tables scroll in their own wrapper.
- Respect `prefers-reduced-motion`. Avoid animation on data tables and keep motion limited to meaningful transitions.
- Check text and control contrast against the actual adjacent background. Token values alone do not prove WCAG compliance.

Static contrast spot checks were calculated from current CSS hex colors using the WCAG relative-luminance formula. `#172033` on white measured 16.27:1. `#64748b` on white measured 4.76:1, but on the muted surface `#eef3f8` it measured 4.26:1. The focus blue `#75a5ff` measured 2.45:1 on white and 2.30:1 on `#f6f8fb`. Danger text `#dc2626` on `#fef3f2` measured 4.44:1. The latter three pairs are below their applicable 4.5:1 text or 3:1 UI contrast target. These are color-pair calculations, not a complete rendered-page accessibility audit.

## Manual review checklist

When browser and backend access are available, verify login/logout, protected-route redirects, each role's allowed pages, student CRUD, search/filter/sort/page behavior, delete confirmation, and network/401/403/500 states. At 360px, 768px, and 1280px check page overflow, table containment, and touch controls. Complete a keyboard-only pass through navigation, menus, dialogs, forms, and pagination; measure contrast and run axe or Lighthouse when available. Record results as measured and keep backend-dependent behavior unverified when its services are unavailable.

## Known source-level gaps

The teacher dashboard shows academic-year, subject, and performance filters, but `applyPerformanceFilters()` currently applies only search, class, and grade. Its “Needs Attention” choice also does not select a separate subset. The admin dashboard's academic-year selector has no change handler. Confirm the real performance response fields and backend contract before implementing these filters; do not infer or fabricate results.

Current verification limitation: the frontend package exposes build/dev/preview scripts only; no lint or test framework is configured. A successful production build does not verify live API behavior, browser console output, responsive rendering, or accessibility scores.
