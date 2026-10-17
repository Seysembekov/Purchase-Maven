package purchase.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import purchase.domain.DuplicatePurchase;
import purchase.domain.Purchase;
import purchase.domain.PurchaseId;
import purchase.persistence.PurchaseJdbc;

import javax.sql.DataSource;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TransactionDemoTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PurchaseService service;

    @Autowired
    private PurchaseJdbc purchases;

    @BeforeEach
    void resetTable() {
        jdbc.execute("DROP TABLE IF EXISTS purchase_request");

        new ResourceDatabasePopulator(
                new ClassPathResource("db/schema.sql")
        ).execute(dataSource);
    }
    @Test
    void demonstratesRollbackAndCommit() {
        assertThrows(
                DuplicatePurchase.class,
                () -> service.insertTwice("DEMO-ROLLBACK")
        );
        int afterRollback = purchases.count("DEMO-ROLLBACK");
        System.out.println("Rows after rollback: " + afterRollback);
        assertEquals(0, afterRollback);

        service.register(purchase("DEMO-COMMIT", "Laptop"));

        System.out.println("First registration committed.");

        assertThrows(
                DuplicatePurchase.class,
                () -> service.register(
                        purchase("DEMO-COMMIT", "Monitor")
                )
        );

        int afterSecondRequest = purchases.count("DEMO-COMMIT");

        System.out.println("Rows after second request fails: " + afterSecondRequest);

        assertEquals(1, afterSecondRequest);

    }

    private Purchase purchase(String key, String productName) {
        return new Purchase(PurchaseId.newId(), key, productName, 1, new BigDecimal("100.00")
        );
    }
}