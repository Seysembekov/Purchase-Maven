
package purchase.domain.rule;

import purchase.domain.Purchase;
import purchase.domain.status.Completed;
import purchase.domain.status.PurchaseStatus;

public class CompletionRule implements PurchaseRule {

    @Override
    public void check(Purchase purchase, PurchaseStatus targetStatus) {
        if (targetStatus instanceof Completed
                && !purchase.isDeliveryConfirmed()) {
            throw new IllegalStateException("Delivery isn't confirmed");
        }
    }
}
