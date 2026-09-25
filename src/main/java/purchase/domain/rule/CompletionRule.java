package purchase.domain.rule;

import purchase.domain.Purchase;
import purchase.domain.PurchaseId;
import purchase.domain.PurchaseStatus;
import purchase.domain.status.Completed;

import java.util.HashSet;
import java.util.Set;

public class CompletionRule implements PurchaseRule {
    private final Set<PurchaseId> deliveredPurchases = new HashSet<>();
    @Override
    public void check(Purchase purchase, PurchaseStatus targetStatus) {
        if (targetStatus instanceof Completed
                && !isDelivered(purchase)) {
            throw new IllegalStateException(
                    "Delivery is not confirmed"
            );
        }
    }
    public void confirmDelivery(PurchaseId purchaseId) {
        deliveredPurchases.add(purchaseId);
    }
    private boolean isDelivered(Purchase purchase) {
        return deliveredPurchases.contains(
                purchase.getId()
        );
    }
}