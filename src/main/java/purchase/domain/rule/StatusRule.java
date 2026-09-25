package purchase.domain.rule;

import purchase.domain.Purchase;
import purchase.domain.PurchasePolicy;
import purchase.domain.PurchaseStatus;

public class StatusRule implements PurchaseRule {
    private final PurchasePolicy policy;
    public StatusRule(PurchasePolicy policy) {
        this.policy = policy;
    }
    @Override
    public void check(Purchase purchase, PurchaseStatus targetStatus) {
        if (isTransitionAllowed(purchase, targetStatus) == false) {
            throw new IllegalStateException("Invalid purchase status transition");
        }
    }
    private boolean isTransitionAllowed(Purchase purchase, PurchaseStatus targetStatus) {
        return policy.canTransition(purchase.getStatus(), targetStatus);
    }
}