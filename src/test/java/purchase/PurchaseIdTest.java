package purchase;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PurchaseIdTest {

    @Test
    void shouldCreateValidPurchaseId() {
        PurchaseId id = new PurchaseId("PUR-001");
        assertEquals("PUR-001", id.value());
    }

    @Test
    void shouldRejectNullId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PurchaseId(null)
        );
    }

    @Test
    void shouldRejectBlankId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PurchaseId("   "));
    }
}