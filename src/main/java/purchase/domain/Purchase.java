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

    private boolean managerApproved;
    private boolean stockAvailable;
    private boolean deliveryConfirmed;

    public Purchase(
            PurchaseId id,
            String businessKey,
            String productName,
            int quantity,
            BigDecimal price
    ) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "PurchaseId cannot be null"
            );
        }

        if (businessKey == null || businessKey.isBlank()) {
            throw new IllegalArgumentException(
                    "Business key cannot be empty"
            );
        }

        this.id = id;
        this.businessKey = businessKey;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;

        this.status = new Draft();

        this.managerApproved = false;
        this.stockAvailable = false;
        this.deliveryConfirmed = false;
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

    public boolean isManagerApproved() {
        return managerApproved;
    }

    public boolean isStockAvailable() {
        return stockAvailable;
    }

    public boolean isDeliveryConfirmed() {
        return deliveryConfirmed;
    }

    public void changeStatus(PurchaseStatus status) {
        if (status == null) {
            throw new IllegalArgumentException(
                    "Status cannot be null"
            );
        }

        this.status = status;
    }

    public void markManagerApproved() {
        this.managerApproved = true;
    }

    public void markStockAvailable() {
        this.stockAvailable = true;
    }

    public void confirmDelivery() {
        this.deliveryConfirmed = true;
    }
}
