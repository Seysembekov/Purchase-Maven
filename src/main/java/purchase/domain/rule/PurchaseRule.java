package purchase.domain.rule;

import purchase.domain.Purchase;
import purchase.domain.status.PurchaseStatus;

public interface PurchaseRule {
    void check(Purchase purchase, PurchaseStatus targetStatus);
}
