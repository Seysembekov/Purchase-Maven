package purchase.domain;

import java.util.UUID;

public record PurchaseId(String value) {
    public PurchaseId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("PurchaseId can't be null or blank");
        }
        try {
            UUID parsed = UUID.fromString(value);
            if (!parsed.toString().equalsIgnoreCase(value)) {
                throw new IllegalArgumentException("PurchaseId must contain a canonical UUID");
            }
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("PurchaseId must contain a valid UUID", exception);
        }
    }

    public static PurchaseId newId() {
        return new PurchaseId(UUID.randomUUID().toString());
    }
}