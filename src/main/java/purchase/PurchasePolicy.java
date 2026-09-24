package purchase;

public class PurchasePolicy {
    public boolean canTransition(PurchaseStatus from, PurchaseStatus to) {

        return switch (from) {
            case Draft ignored-> to instanceof Submitted;
            case Submitted ignored-> to instanceof Approved;
            case Approved ignored -> to instanceof Ordered;
            case Ordered ignored -> to instanceof Completed;
            case Completed ignored -> false;};
    }
}