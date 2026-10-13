package purchase.config;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import purchase.domain.Purchase;
import purchase.domain.PurchaseId;
import purchase.domain.rule.ApprovalRule;
import purchase.domain.rule.CompletionRule;
import purchase.domain.rule.OrderRule;
import purchase.domain.status.Approved;
import purchase.domain.status.Completed;
import purchase.domain.status.Ordered;
import purchase.domain.status.Submitted;
import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        PurchaseService purchaseService = context.getBean(PurchaseService.class);
        ApprovalRule approvalRule = context.getBean(ApprovalRule.class);
        OrderRule orderRule = context.getBean(OrderRule.class);
        CompletionRule completionRule = context.getBean(CompletionRule.class);

        Purchase purchase = new Purchase(new PurchaseId("aaa-001"), "Laptop", 1, new BigDecimal("1000"));

        purchaseService.move(purchase, new Submitted());
        System.out.println("DRAFT -> SUBMITTED");

        approvalRule.approve(purchase.getId());
        purchaseService.move(purchase, new Approved());
        System.out.println("SUBMITTED -> APPROVED");

        orderRule.markAvailable(purchase.getId());
        purchaseService.move(purchase, new Ordered());
        System.out.println("APPROVED -> ORDERED");

        completionRule.confirmDelivery(purchase.getId());
        purchaseService.move(purchase, new Completed());
        System.out.println("ORDERED -> COMPLETED");

        System.out.println("WORKFLOW COMPLETED");
        context.close();
    }
}