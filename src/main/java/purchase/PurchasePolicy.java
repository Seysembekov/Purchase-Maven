package purchase;

public final class PurchasePolicy {

    private PurchasePolicy() {
    }

    public static PurchaseStatus move(
            PurchaseStatus from, PurchaseStatus to) {
        if (from instanceof Draft && to instanceof Approved) {
            return to;
        }

        if (from instanceof Approved && to instanceof Ordered) {
            return to;
        }

        throw new IllegalStateException(
                "Forbidden purchase status transition: "
                        + from.getClass().getSimpleName()
                        + " -> "
                        + to.getClass().getSimpleName()
        );
    }
}