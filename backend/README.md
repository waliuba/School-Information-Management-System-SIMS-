# SIMS - School Information Management System

A School Information Management System built as a fullstack 
development project to demonstrate React, Java, Spring Boot, Oracle
Database, JPA/Hibernate, REST APIs, and software architecture.

## Technologies
- React
- Java
- Spring Boot
- Spring Data JPA
- Oracle Database
- Maven
- REST API
- Hibernate

## Architecture

The backend follows a layered architecture:

Controller
    ↓
Service
    ↓
Repository
    ↓
Oracle Database

## Main Modules

- Students
- Departments
- Classes
- Subjects/Units
- Enrollments
- Users
- Authentication

## Database

The system uses Oracle Database with relational tables,
primary keys, foreign keys, constraints, and relationships.

## Settings APIs

- `GET` and `PUT /api/settings/me/preferences` read and update the signed-in user's appearance preferences.
- `GET` and `PUT /api/settings/school-profile` read and update the single-school profile. Until a school entity is introduced, the profile uses `SIMS_SETTING` scope `SCHOOL` and owner ID `1`.
- `GET /api/settings/system`, `POST /api/settings/system`, `PUT /api/settings/system/{key}`, and `DELETE /api/settings/system/{key}` provide audited CRUD for non-secret system settings, stored in scope `SYSTEM` and owner ID `1`.
- `GET /api/settings/system-health` reports authenticated application, database, authentication, and audit-storage checks. `GET /api/health` is a public liveness probe and does not check database readiness.
- `GET /api/settings/audit` returns up to the 100 most recent audit-event summaries; event details are not exposed by this endpoint.

Settings management, school profile, audit, and detailed health endpoints require an administrator role. Apply the Oracle migration in `db/migration/V1__settings_foundation.sql` before using settings persistence or audit endpoints.

## Project Purpose

This project was developed as a portfolio project to demonstrate
backend development skills, database design, Java programming,
REST API development, and layered application architecture.

## License

Copyright © 2026 Xavier Waliuba.
All Rights Reserved.

The source code may be viewed for portfolio, educational,
and evaluation purposes. Reuse, redistribution, and
commercial use require permission.
