package purchase;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import purchase.domain.Purchase;
import purchase.domain.PurchaseId;
import purchase.domain.rule.PurchasePolicy;
import purchase.domain.status.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PurchasePolicyTest {

    private final PurchasePolicy policy = new PurchasePolicy();

    @ParameterizedTest
    @CsvSource({
            "DRAFT, SUBMITTED, true",
            "SUBMITTED, APPROVED, true",
            "DRAFT, ORDERED, false",
            "ORDERED, DRAFT, false"
    })
    void checksTransitions(
            String from,
            String to,
            boolean allowed
    ) {
        Purchase purchase = new Purchase(
                PurchaseId.newId(),
                "PR-TEST",
                "Laptop",
                1,
                new BigDecimal("10.00")
        );

        purchase.changeStatus(status(from));

        if (allowed) {
            assertDoesNotThrow(
                    () -> policy.check(purchase, status(to))
            );
        } else {
            assertThrows(
                    IllegalStateException.class,
                    () -> policy.check(purchase, status(to))
            );
        }
    }

    private PurchaseStatus status(String name) {
        return switch (name) {
            case "DRAFT" -> new Draft();
            case "SUBMITTED" -> new Submitted();
            case "APPROVED" -> new Approved();
            case "ORDERED" -> new Ordered();
            case "COMPLETED" -> new Completed();
            default -> throw new IllegalArgumentException(name);
        };
    }
}