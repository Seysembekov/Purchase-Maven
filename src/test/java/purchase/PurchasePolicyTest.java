package purchase;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PurchasePolicyTest {

    @ParameterizedTest
    @CsvSource({
            "Draft, Approved, true",
            "Approved, Ordered, true",
            "Draft, Ordered, false",
            "Ordered, Draft, false"
    })
    void shouldHandleStatusTransitions(
            String fromName,
            String toName,
            boolean allowed
    ) {
        PurchaseStatus from = status(fromName);
        PurchaseStatus to = status(toName);

        if (allowed) {
            PurchaseStatus result = PurchasePolicy.move(from, to);

            assertEquals(to, result);
        } else {
            assertThrows(
                    IllegalStateException.class,
                    () -> PurchasePolicy.move(from, to)
            );
        }
    }

    private PurchaseStatus status(String name) {
        return switch (name) {
            case "Draft" -> new Draft();
            case "Approved" -> new Approved();
            case "Ordered" -> new Ordered();
            default -> throw new IllegalArgumentException(
                    "Unknown status: " + name
            );
        };
    }
}