package purchase.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import purchase.domain.Purchase;
import purchase.domain.PurchaseId;
import purchase.domain.rule.ApprovalRule;
import purchase.domain.rule.CompletionRule;
import purchase.domain.rule.OrderRule;
import purchase.domain.status.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PurchaseServiceTest {
    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private ApprovalRule approvalRule;
    @Autowired
    private OrderRule orderRule;
    @Autowired
    private CompletionRule completionRule;
    @Test
    void purchaseCanCompleteFullWorkflow() {

        Purchase purchase = new Purchase(new PurchaseId("l-12341234"), "Laptop", 1, new BigDecimal("1000"));
        purchaseService.move(purchase, new Submitted());

        approvalRule.approve(purchase.getId());

        purchaseService.move(purchase, new Approved());

        orderRule.markAvailable(purchase.getId());

        purchaseService.move(purchase, new Ordered());

        completionRule.confirmDelivery(purchase.getId());

        purchaseService.move(purchase,new Completed());

        assertEquals(new Completed(), purchase.getStatus());
    }
    @Test
    void cannotApproveWithoutManagerApproval() {
        Purchase purchase = new Purchase(new PurchaseId("PUR-002"), "Laptop", 1, new BigDecimal("1000"));
        purchaseService.move(purchase, new Submitted());
        assertThrows(IllegalStateException.class, () -> purchaseService.move(purchase, new Approved()));
    }
}