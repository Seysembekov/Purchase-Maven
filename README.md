# Purchase Management

## Product

Purchase Management is a small Java project that describes the lifecycle of a purchase.

## Core item

The core item is a purchase. It has a validated identifier and one of three statuses: `Draft`, `Approved`, or `Ordered`.

## Status table

| From     | To       | Result    |
|----------|----------|-----------|
| Draft    | Approved | Allowed   |
| Approved | Ordered  | Allowed   |
| Draft    | Ordered  | Forbidden |
| Ordered  | Draft    | Forbidden |

## Forbidden — why

`Draft → Ordered` is forbidden because a purchase must be approved before it can be ordered.

`Ordered → Draft` is forbidden because an ordered purchase cannot return to the draft stage.

**Run tests**

```bash
mvn test