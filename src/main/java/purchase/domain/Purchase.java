package purchase.domain;
public class Purchase {
    private final PurchaseId id;
    private final String productName;
    private final String supplierName;
    private final int quantity;
    private final Float unitPrice;
    private PurchaseStatus status;

    public Purchase(PurchaseId id, String productName,
                    String supplierName, int quantity,
                    Float unitPrice) {

        this.id = id;
        this.productName = productName;
        this.supplierName = supplierName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.status = new Draft();
    }

    public PurchaseId getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public int getQuantity() {
        return quantity;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public PurchaseStatus getStatus() {
        return status;
    }

    public Float getTotalPrice() {
        return unitPrice * quantity;

    }

    public void changeStatus(PurchaseStatus status) {
        this.status = status;
    }
}
