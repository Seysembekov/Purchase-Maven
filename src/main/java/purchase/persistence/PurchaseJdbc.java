
package purchase.persistence;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import purchase.domain.Purchase;
import purchase.domain.PurchaseId;
import purchase.domain.status.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PurchaseJdbc {
    private final JdbcTemplate jdbc;

    public PurchaseJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void insert(Purchase purchase) {
        jdbc.update(
                """
                INSERT INTO purchase_request
                    (id, business_key, status,
                     product_name, quantity, price,
                     manager_approved, stock_available, delivery_confirmed)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """,
                UUID.fromString(purchase.getId().value()),
                purchase.getBusinessKey(),
                statusText(purchase.getStatus()),
                purchase.getProductName(),
                purchase.getQuantity(),
                purchase.getPrice(),
                purchase.isManagerApproved(),
                purchase.isStockAvailable(),
                purchase.isDeliveryConfirmed()
        );
    }

    public int count(String businessKey) {
        Integer count = jdbc.queryForObject(
                """
                SELECT COUNT(*)
                FROM purchase_request
                WHERE business_key = ?
                """,
                Integer.class,
                businessKey
        );

        return count == null ? 0 : count;
    }

    public Optional<Purchase> find(String businessKey) {
        List<Purchase> result = jdbc.query(
                """
                SELECT id, business_key, status,
                       product_name, quantity, price,
                       manager_approved, stock_available, delivery_confirmed
                FROM purchase_request
                WHERE business_key = ?
                """,
                (rs, row) -> {
                    Purchase purchase = new Purchase(
                            new PurchaseId(rs.getObject("id", UUID.class).toString()),
                            rs.getString("business_key"),
                            rs.getString("product_name"),
                            rs.getInt("quantity"),
                            rs.getBigDecimal("price")
                    );
                    purchase.changeStatus(readStatus(rs.getString("status"))
                    );
                    if (rs.getBoolean("manager_approved")) {
                        purchase.markManagerApproved();
                    }
                    if (rs.getBoolean("stock_available")) {
                        purchase.markStockAvailable();
                    }
                    if (rs.getBoolean("delivery_confirmed")) {
                        purchase.confirmDelivery();
                    }

                    return purchase;
                },
                businessKey
        );

        return result.stream().findFirst();
    }

    private String statusText(PurchaseStatus status) {
        if (status instanceof Draft) return "DRAFT";
        if (status instanceof Submitted) return "SUBMITTED";
        if (status instanceof Approved) return "APPROVED";
        if (status instanceof Ordered) return "ORDERED";
        if (status instanceof Completed) return "COMPLETED";

        throw new IllegalArgumentException("Unknown status");
    }

    private PurchaseStatus readStatus(String status) {
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
