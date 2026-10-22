package purchase.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "purchase_request")
public class PurchaseEntity {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(
            name = "business_key",
            nullable = false,
            unique = true,
            columnDefinition = "text"
    )
    private String businessKey;

    @Column(
            name = "status",
            nullable = false,
            columnDefinition = "text"
    )
    private String status;

    @Column(
            name = "product_name",
            nullable = false,
            columnDefinition = "text"
    )
    private String productName;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(
            name = "price",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal price;

    @Column(name = "manager_approved", nullable = false)
    private boolean managerApproved;

    @Column(name = "stock_available", nullable = false)
    private boolean stockAvailable;

    @Column(name = "delivery_confirmed", nullable = false)
    private boolean deliveryConfirmed;

    @Column(
            name = "created_at",
            nullable = false,
            insertable = false,
            updatable = false
    )
    private OffsetDateTime createdAt;

    protected PurchaseEntity() {
        // Required by JPA.
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isManagerApproved() {
        return managerApproved;
    }

    public void setManagerApproved(boolean managerApproved) {
        this.managerApproved = managerApproved;
    }

    public boolean isStockAvailable() {
        return stockAvailable;
    }

    public void setStockAvailable(boolean stockAvailable) {
        this.stockAvailable = stockAvailable;
    }

    public boolean isDeliveryConfirmed() {
        return deliveryConfirmed;
    }

    public void setDeliveryConfirmed(boolean deliveryConfirmed) {
        this.deliveryConfirmed = deliveryConfirmed;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}