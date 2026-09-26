package purchase;
import org.junit.jupiter.api.Test;
import purchase.domain.PurchaseId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PurchaseIdTest {

    @Test
    void shouldCreateValidPurchaseId() {PurchaseId id = new PurchaseId("aaa1234");
        assertEquals("aaa1234", id.value());
    }
    @Test
    void shouldRejectNullId() {
        assertThrows(IllegalArgumentException.class, () -> new PurchaseId(null));
    }
    @Test
    void shouldRejectBlankId() {
        assertThrows(IllegalArgumentException.class, () -> new PurchaseId("   "));
    }

}
//    @Test
//    void shouldFailTest() {
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> new PurchaseId("PPP-1"));
//    }
//}