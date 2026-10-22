package purchase.domain.rule;

import purchase.domain.Purchase;
import purchase.domain.status.PurchaseStatus;
import purchase.domain.status.Submitted;

public class DraftRule implements PurchaseRule {

    @Override
    public void check(
            Purchase purchase,
            PurchaseStatus targetStatus
    ) {
        if (targetStatus instanceof Submitted) {
            validateProduct(purchase);
            validateQuantity(purchase);
            validatePrice(purchase);
        }
    }

    private void validateProduct(Purchase purchase) {
        if (purchase.getProductName() == null
                || purchase.getProductName().isBlank()) {
            throw new IllegalStateException(
                    "Product name is required"
            );
        }
    }

    private void validateQuantity(Purchase purchase) {
        if (purchase.getQuantity() <= 0) {
            throw new IllegalStateException(
                    "Quantity must be greater than zero"
            );
        }
    }

    private void validatePrice(Purchase purchase) {
        if (purchase.getPrice() == null
                || purchase.getPrice().signum() < 0) {
            throw new IllegalStateException(
                    "Price must be zero or greater"
            );
        }
    }
}