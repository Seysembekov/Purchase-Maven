package purchase.domain;

import java.math.BigDecimal;

public class Purchase {

    private final PurchaseId id;
    private final String productName;
    private final int quantity;
    private final BigDecimal price;

    private PurchaseStatus status;

    public Purchase(
            PurchaseId id,
            String productName,
            int quantity,
            BigDecimal price
    ) {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.status = new Draft();
    }

    public PurchaseId getId() {
        return id;
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
        this.status = status;
    }
}