package purchase.config;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import purchase.domain.Purchase;
import purchase.domain.PurchaseId;
import purchase.domain.rule.ApprovalRule;
import purchase.domain.rule.CompletionRule;
import purchase.domain.rule.OrderRule;
import purchase.domain.status.*;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(Application.class, args);

        PurchaseService service =
                context.getBean(PurchaseService.class);

        ApprovalRule approval =
                context.getBean(ApprovalRule.class);

        OrderRule order =
                context.getBean(OrderRule.class);

        CompletionRule completion =
                context.getBean(CompletionRule.class);

        Purchase purchase = new Purchase(
                PurchaseId.newId(),
                "PR-1",
                "Laptop",
                1,
                new BigDecimal("1000.00")
        );

        service.move(purchase, new Submitted());
        approval.approve(purchase.getId());
        service.move(purchase, new Approved());

        order.markAvailable(purchase.getId());
        service.move(purchase, new Ordered());

        completion.confirmDelivery(purchase.getId());
        service.move(purchase, new Completed());

        System.out.println("WORKFLOW COMPLETED");
        context.close();
    }
}