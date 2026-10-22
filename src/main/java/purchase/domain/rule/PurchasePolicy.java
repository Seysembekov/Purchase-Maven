package purchase.domain.rule;

import purchase.domain.Purchase;
import purchase.domain.status.*;

public class PurchasePolicy implements PurchaseRule {

    @Override
    public void check(
            Purchase purchase,
            PurchaseStatus targetStatus
    ) {
        if (purchase == null || targetStatus == null) {
            throw new IllegalArgumentException(
                    "Purchase and target status are required"
            );
        }

        if (!canTransition(
                purchase.getStatus(),
                targetStatus
        )) {
            throw new IllegalStateException(
                    "Invalid purchase status transition"
            );
        }
    }

    public boolean canTransition(
            PurchaseStatus from,
            PurchaseStatus to
    ) {
        if (from == null || to == null) {
            return false;
        }

        if (from instanceof Draft) {
            return to instanceof Submitted;
        }

        if (from instanceof Submitted) {
            return to instanceof Approved;
        }

        if (from instanceof Approved) {
            return to instanceof Ordered;
        }

        if (from instanceof Ordered) {
            return to instanceof Completed;
        }

        if (from instanceof Completed) {
            return false;
        }

        return false;
    }
}