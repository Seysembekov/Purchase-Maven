package purchase.domain;

import purchase.domain.status.Draft;
import purchase.domain.status.PurchaseStatus;

import java.math.BigDecimal;

public class Purchase {

    private final PurchaseId id;
    private final String businessKey;
    private final String productName;
    private final int quantity;
    private final BigDecimal price;

    private PurchaseStatus status;

    public Purchase(
            PurchaseId id,
            String businessKey,
            String productName,
            int quantity,
            BigDecimal price
    ) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "PurchaseId can't be null"
            );
        }

        if (businessKey == null || businessKey.isBlank()) {
            throw new IllegalArgumentException(
                    "Business key can't be empty"
            );
        }

        this.id = id;
        this.businessKey = businessKey;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.status = new Draft();
    }

    public PurchaseId getId() {
        return id;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public PurchaseStatus getStatus() {
        return status;
    }

    public void changeStatus(PurchaseStatus status) {
        if (status == null) {
            throw new IllegalArgumentException(
                    "Status can't be null"
            );
        }

        this.status = status;
    }
}