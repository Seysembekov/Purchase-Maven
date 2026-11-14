
package purchase.domain;

public class PurchaseNotFound extends RuntimeException {

    public PurchaseNotFound(PurchaseId id) {
        super("Purchase not found: " + id.value());
    }
}
