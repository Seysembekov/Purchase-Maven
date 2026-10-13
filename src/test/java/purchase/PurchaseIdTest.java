package purchase;

import org.junit.jupiter.api.Test;
import purchase.domain.PurchaseId;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseIdTest {

    @Test
    void createsUuid() {
        PurchaseId id = PurchaseId.newId();

        assertEquals(
                UUID.fromString(id.value()).toString(),
                id.value()
        );
    }

    @Test
    void acceptsValidUuid() {
        String value = UUID.randomUUID().toString();

        assertEquals(
                value,
                new PurchaseId(value).value()
        );
    }

    @Test
    void rejectsNullId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PurchaseId(null)
        );
    }

    @Test
    void rejectsBlankId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PurchaseId(" ")
        );
    }

    @Test
    void rejectsInvalidUuid() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PurchaseId("aaa-001")
        );
    }
}