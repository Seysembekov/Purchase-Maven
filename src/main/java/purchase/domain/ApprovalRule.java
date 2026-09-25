package purchase.domain;

import java.util.HashSet;
import java.util.Set;

public class ApprovalRule implements PurchaseRule {

    private final Set<PurchaseId> approvedPurchases = new HashSet<>();
    @Override
    public void check(Purchase purchase, PurchaseStatus targetStatus) {
        if (targetStatus instanceof Approved && !isManagerApproved(purchase)) {
            throw new IllegalStateException(
                    "Manager approval is required"
            );
        }
    }

    public void approve(PurchaseId purchaseId) {
        approvedPurchases.add(purchaseId);
    }

    private boolean isManagerApproved(Purchase purchase) {
        return approvedPurchases.contains(
                purchase.getId()
        );
    }
}