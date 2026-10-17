
package purchase.domain.rule;

import purchase.domain.Purchase;
import purchase.domain.status.Ordered;
import purchase.domain.status.PurchaseStatus;

public class OrderRule implements PurchaseRule {

    @Override
    public void check(Purchase purchase, PurchaseStatus targetStatus) {
        if (targetStatus instanceof Ordered
                && !purchase.isStockAvailable()) {
            throw new IllegalStateException("Product is not available");
        }
    }
}
