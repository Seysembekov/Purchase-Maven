package purchase;

public sealed interface PurchaseStatus
        permits Draft, Submitted, Approved, Ordered, Completed {
}