
package purchase.domain;

public class InvalidPurchaseId extends RuntimeException {

    public InvalidPurchaseId() {
        super("id must be a UUID");
    }
}
