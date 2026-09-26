package purchase.domain;

public record PurchaseId(String value) {

    public PurchaseId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("PurchaseId can't be null");}
    }
}