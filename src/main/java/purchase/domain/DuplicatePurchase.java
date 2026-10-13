package purchase.domain;

public class DuplicatePurchase extends RuntimeException {

    public DuplicatePurchase(String businessKey, Throwable cause) {
        super("Purchase already exists: " + businessKey, cause);
    }
}