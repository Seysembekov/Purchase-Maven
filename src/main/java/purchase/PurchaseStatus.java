package purchase;

public sealed interface PurchaseStatus
        permits Draft, Approved, Ordered {
}