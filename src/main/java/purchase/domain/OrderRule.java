package purchase.domain;

import java.util.HashSet;
import java.util.Set;

public class OrderRule implements PurchaseRule {

    private final Set<PurchaseId> availablePurchases = new HashSet<>();
    @Override
    public void check(Purchase purchase, PurchaseStatus targetStatus) {
        if (targetStatus instanceof Ordered && !isAvailable(purchase)) {
            throw new IllegalStateException(
                    "Product is not available"
            );
        }
    }
    public void markAvailable(PurchaseId purchaseId) {
        availablePurchases.add(purchaseId);
    }
    private boolean isAvailable(Purchase purchase) {
        return availablePurchases.contains(purchase.getId());
    }
}