package purchase.domain;

public interface PurchaseRule {
    void check(Purchase purchase, PurchaseStatus targetStatus);
}
