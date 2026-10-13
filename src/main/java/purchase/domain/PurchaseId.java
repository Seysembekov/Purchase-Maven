package purchase.domain;

import java.util.UUID;

public record PurchaseId(String value) {

    public PurchaseId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "PurchaseId can't be empty"
            );
        }

        UUID uuid = UUID.fromString(value);

        if (!uuid.toString().equalsIgnoreCase(value)) {
            throw new IllegalArgumentException(
                    "Invalid UUID format"
            );
        }
    }

    public static PurchaseId newId() {
        return new PurchaseId(
                UUID.randomUUID().toString()
        );
    }
}