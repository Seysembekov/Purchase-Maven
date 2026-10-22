package purchase.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import purchase.domain.DuplicatePurchase;
import purchase.domain.Purchase;
import purchase.domain.PurchaseId;
import purchase.persistence.PurchaseJpa;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionDemoTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private PurchaseService service;

    @Autowired
    private PurchaseJpa purchases;

    @BeforeEach
    void cleanRows() {
        jdbc.update("DELETE FROM purchase_request");
    }

    @Test
    void demonstratesRollbackAndCommit() {
        assertThrows(
                DuplicatePurchase.class,
                () -> service.insertTwice("DEMO-ROLLBACK")
        );

        int afterRollback =
                purchases.count("DEMO-ROLLBACK");

        System.out.println(
                "Rows after rollback: " + afterRollback
        );

        assertEquals(0, afterRollback);

        service.register(
                purchase("DEMO-COMMIT", "Laptop")
        );

        System.out.println("First registration committed.");

        assertThrows(
                DuplicatePurchase.class,
                () -> service.register(
                        purchase("DEMO-COMMIT", "Monitor")
                )
        );

        int afterSecondRequest =
                purchases.count("DEMO-COMMIT");

        System.out.println(
                "Rows after second request fails: "
                        + afterSecondRequest
        );

        assertEquals(1, afterSecondRequest);
    }

    private Purchase purchase(
            String key,
            String productName
    ) {
        return new Purchase(
                PurchaseId.newId(),
                key,
                productName,
                1,
                new BigDecimal("100.00")
        );
    }
}