package purchase.domain;

import purchase.domain.status.Approved;
import purchase.domain.status.Completed;
import purchase.domain.status.Draft;
import purchase.domain.status.Ordered;
import purchase.domain.status.Submitted;

public class PurchasePolicy {
    public boolean canTransition(PurchaseStatus from, PurchaseStatus to) {
        if (from instanceof Draft) {
            return to instanceof Submitted;}
        if (from instanceof Submitted) {
            return to instanceof Approved;}
        if (from instanceof Approved) {
            return to instanceof Ordered;
        }
        if (from instanceof Ordered) {
            return to instanceof Completed;
        }
        if (from instanceof Completed) {
            return false;}

        return false;}
}