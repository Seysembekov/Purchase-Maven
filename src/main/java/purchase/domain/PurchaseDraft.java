package purchase.domain;

public class PurchaseDraft implements PurchaseRule {
    @Override
    public void check(Purchase purchase, PurchaseStatus targetStatus) {
        if (!(targetStatus instanceof Submitted)) {
            return;
        }
        if (purchase.getProductName() == null || purchase.getProductName().isBlank()) {
            throw new IllegalStateException("Product name is required");
        }

        if (purchase.getSupplierName() == null || purchase.getSupplierName().isBlank()) {
            throw new IllegalStateException("Supplier name is required");
        }
        if (purchase.getQuantity() <= 0) {
            throw new IllegalStateException("Quantity must be greater than zero");
        }
        if (purchase.getUnitPrice() == null || purchase.getUnitPrice() <= 0) {
            throw new IllegalStateException("Unit price must be greater than zero");
        }
    }
}