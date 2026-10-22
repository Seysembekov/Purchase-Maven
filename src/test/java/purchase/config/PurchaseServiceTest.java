package purchase.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import purchase.domain.Purchase;
import purchase.domain.PurchaseId;
import purchase.domain.rule.PurchaseRule;
import purchase.domain.status.*;
import purchase.persistence.PurchaseJpa;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PurchaseServiceTest {

    @Autowired
    private PurchaseService service;

    @Test
    void purchaseCanCompleteWorkflow() {
        Purchase purchase = purchase("PR-FLOW");

        service.move(purchase, new Submitted());

        purchase.markManagerApproved();
        service.move(purchase, new Approved());

        purchase.markStockAvailable();
        service.move(purchase, new Ordered());

        purchase.confirmDelivery();
        service.move(purchase, new Completed());

        assertEquals(
                new Completed(),
                purchase.getStatus()
        );
    }

    @Test
    void cannotApproveWithoutApproval() {
        Purchase purchase = purchase("PR-APPROVAL");

        service.move(purchase, new Submitted());

        assertThrows(
                IllegalStateException.class,
                () -> service.move(
                        purchase,
                        new Approved()
                )
        );
    }

    @Test
    void sendsStatusToMockedPort() {
        PurchaseRule rules = mock(PurchaseRule.class);
        PurchaseJpa db = mock(PurchaseJpa.class);
        PurchasePort port = mock(PurchasePort.class);

        PurchaseService testService =
                new PurchaseService(rules, db, port);

        Purchase purchase = purchase("PR-MOCK");

        testService.move(purchase, new Submitted());

        verify(port).statusChanged(
                purchase.getId(),
                new Submitted()
        );

        verifyNoMoreInteractions(port);
    }

    private Purchase purchase(String key) {
        return new Purchase(
                PurchaseId.newId(),
                key,
                "Laptop",
                1,
                new BigDecimal("100.00")
        );
    }
}