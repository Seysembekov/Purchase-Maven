package purchase.persistence;

import org.springframework.stereotype.Component;
import purchase.domain.Purchase;
import purchase.domain.PurchaseId;
import purchase.domain.status.*;

import java.util.UUID;

@Component
public class PurchaseMapper {

    public PurchaseEntity toEntity(Purchase purchase) {
        if (purchase == null) {
            throw new IllegalArgumentException(
                    "Purchase cannot be null"
            );
        }

        PurchaseEntity entity = new PurchaseEntity();

        entity.setId(
                UUID.fromString(purchase.getId().value())
        );

        entity.setBusinessKey(purchase.getBusinessKey());
        entity.setStatus(toDatabaseStatus(purchase.getStatus()));
        entity.setProductName(purchase.getProductName());
        entity.setQuantity(purchase.getQuantity());
        entity.setPrice(purchase.getPrice());

        entity.setManagerApproved(
                purchase.isManagerApproved()
        );

        entity.setStockAvailable(
                purchase.isStockAvailable()
        );

        entity.setDeliveryConfirmed(
                purchase.isDeliveryConfirmed()
        );

        return entity;
    }

    public Purchase toDomain(PurchaseEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException(
                    "PurchaseEntity cannot be null"
            );
        }

        Purchase purchase = new Purchase(
                new PurchaseId(entity.getId().toString()),
                entity.getBusinessKey(),
                entity.getProductName(),
                entity.getQuantity(),
                entity.getPrice()
        );

        purchase.changeStatus(
                fromDatabaseStatus(entity.getStatus())
        );

        if (entity.isManagerApproved()) {
            purchase.markManagerApproved();
        }

        if (entity.isStockAvailable()) {
            purchase.markStockAvailable();
        }

        if (entity.isDeliveryConfirmed()) {
            purchase.confirmDelivery();
        }

        return purchase;
    }

    private String toDatabaseStatus(PurchaseStatus status) {
        if (status instanceof Draft) {
            return "DRAFT";
        }

        if (status instanceof Submitted) {
            return "SUBMITTED";
        }

        if (status instanceof Approved) {
            return "APPROVED";
        }

        if (status instanceof Ordered) {
            return "ORDERED";
        }

        if (status instanceof Completed) {
            return "COMPLETED";
        }

        throw new IllegalArgumentException(
                "Unsupported purchase status: " + status
        );
    }

    private PurchaseStatus fromDatabaseStatus(String status) {
        return switch (status) {
            case "DRAFT" -> new Draft();
            case "SUBMITTED" -> new Submitted();
            case "APPROVED" -> new Approved();
            case "ORDERED" -> new Ordered();
            case "COMPLETED" -> new Completed();

            default -> throw new IllegalStateException(
                    "Unknown status in database: " + status
            );
        };
    }
}