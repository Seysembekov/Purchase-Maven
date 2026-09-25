package purchase.domain;

public class DraftRule implements PurchaseRule {
    @Override
    public void check(Purchase purchase, PurchaseStatus targetStatus) {
        if (targetStatus instanceof Submitted) {
            validateProduct(purchase);
            validateQuantity(purchase);
        }
    }
    private void validateProduct(Purchase purchase) {
        if (purchase.getProductName() == null || purchase.getProductName().isBlank()) {
            throw new IllegalStateException("Product name is required");
        }
    }

    private void validateQuantity(Purchase purchase) {
        if (purchase.getQuantity() <= 0) {
            throw new IllegalStateException(
                    "Quantity must be greater than zero"
            );
        }
    }
}