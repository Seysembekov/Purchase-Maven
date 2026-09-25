package purchase.domain;

public sealed interface PurchaseStatus
        permits Draft, Submitted, Approved, Ordered, Completed {
}