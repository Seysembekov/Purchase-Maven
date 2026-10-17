
# Purchase Management

## Product

Purchase Management is a small Java project that describes the lifecycle of a purchase.

## Core item

A purchase has a validated UUID and five statuses:
DRAFT, SUBMITTED, APPROVED, ORDERED, and COMPLETED.

## Lifecycle

DRAFT → SUBMITTED → APPROVED → ORDERED → COMPLETED

## Status table

| From      | To        | Result    |
|-----------|-----------|-----------|
| DRAFT     | SUBMITTED | Allowed   |
| SUBMITTED | APPROVED  | Allowed   |
| DRAFT     | ORDERED   | Forbidden |
| ORDERED   | DRAFT     | Forbidden |

## Forbidden — why

DRAFT → ORDERED is forbidden because a purchase
must pass the required stages before ordering.

ORDERED → DRAFT is forbidden because an ordered
purchase cannot return to the draft stage.

## Tests

Run the tests against PostgreSQL:

```bash
mvn -q verify
```
