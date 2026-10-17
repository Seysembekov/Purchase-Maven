
package purchase.domain.rule;

import purchase.domain.Purchase;
import purchase.domain.status.Approved;
import purchase.domain.status.PurchaseStatus;

public class ApprovalRule implements PurchaseRule {

    @Override
    public void check(Purchase purchase, PurchaseStatus targetStatus) {
        if (targetStatus instanceof Approved
                && !purchase.isManagerApproved()) {
            throw new IllegalStateException("approval is required");
        }
    }
}
