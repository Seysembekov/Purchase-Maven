
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
import purchase.domain.status.Completed;
import purchase.persistence.PurchaseJdbc;

import javax.sql.DataSource;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PurchaseDBTest {
    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private PurchaseService service;
    @Autowired
    private PurchaseJdbc purchases;
    @BeforeEach
    void createTable() {
        jdbc.execute("DROP TABLE IF EXISTS purchase_request");

        new ResourceDatabasePopulator(
                new ClassPathResource("db/schema.sql")
        ).execute(dataSource);
    }

    @Test
    void secondStatementRollsBack() {
        assertThrows(DuplicatePurchase.class, () -> service.insertTwice("PR-ROLLBACK"));
        assertEquals(0, purchases.count("PR-ROLLBACK"));
    }

    @Test
    void secondRequestKeepsTheFirst() {
        service.register(purchase("PR-19", "Laptop"));
        assertThrows(DuplicatePurchase.class, () -> service.register(purchase("PR-19", "Monitor")));

        assertEquals(1, purchases.count("PR-19"));
    }

    @Test
    void readsSavedPurchaseAndBusinessFacts() {
        Purchase purchase = purchase("PR-READ", "Monitor");
        purchase.markManagerApproved();
        purchase.markStockAvailable();
        purchase.confirmDelivery();
        purchase.changeStatus(new Completed());

        service.register(purchase);

        Purchase saved = purchases.find("PR-READ").orElseThrow();

        assertEquals(purchase.getId(), saved.getId());
        assertEquals(purchase.getProductName(), saved.getProductName());
        assertEquals(purchase.getQuantity(), saved.getQuantity());
        assertEquals(purchase.getPrice(), saved.getPrice());
        assertEquals(purchase.getStatus(), saved.getStatus());
        assertTrue(saved.isManagerApproved());
        assertTrue(saved.isStockAvailable());
        assertTrue(saved.isDeliveryConfirmed());
    }

    private Purchase purchase(String key, String name) {
        return new Purchase(PurchaseId.newId(), key, name, 2, new BigDecimal("125.50")
        );
    }
}
