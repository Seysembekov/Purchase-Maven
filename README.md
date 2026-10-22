# Purchase Management

## Project goal

Purchase Management is a Java 21 project that demonstrates
purchase lifecycle rules, JPA persistence, and database migrations.

The project uses Spring Boot, Hibernate, JPA, PostgreSQL, and Flyway.

There is no REST API in this lab.

## Purchase model

A purchase contains:

- UUID identifier
- unique business key
- product name
- quantity
- price
- manager approval flag
- stock availability flag
- delivery confirmation flag
- status

## Status lifecycle

DRAFT -> SUBMITTED -> APPROVED -> ORDERED -> COMPLETED

Allowed transitions:

| From      | To        | Result  |
|-----------|-----------|---------|
| DRAFT     | SUBMITTED | Allowed |
| SUBMITTED | APPROVED  | Allowed |
| APPROVED  | ORDERED   | Allowed |
| ORDERED   | COMPLETED | Allowed |

Other transitions are forbidden.

## Business rules

- A product name is required before submission.
- Quantity must be greater than zero.
- Price cannot be negative.
- Manager approval is required before APPROVED.
- Stock must be available before ORDERED.
- Delivery must be confirmed before COMPLETED.

## Architecture

### Domain

Contains Purchase, PurchaseId, statuses, policies, and rules.
It does not depend on JPA.

### Persistence

PurchaseEntity maps the PostgreSQL table.
PurchaseMapper translates between the domain model and the entity.
PurchaseJpa uses EntityManager for database operations.

### Service

PurchaseService validates transitions and controls registration
transactions. Duplicate business keys are translated to
DuplicatePurchase.

### Database migrations

Flyway owns the schema through:

src/main/resources/db/migration/V1__create_purchase_request.sql

Hibernate uses ddl-auto=validate.
Hibernate does not create or update the schema.

## Transaction tests

The project checks two scenarios:

1. Two inserts with the same business key inside one transaction
   must roll back, leaving zero records.

2. A successful first registration remains committed when a
   separate second registration fails, leaving one record.

## Run tests

Make sure PostgreSQL is running and the database connection
matches application.properties or the DB_* environment variables.

Run:

mvn -q verify