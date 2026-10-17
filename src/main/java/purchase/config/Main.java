
package purchase.config;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import purchase.domain.Purchase;
import purchase.domain.PurchaseId;
import purchase.domain.status.*;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        PurchaseService service = context.getBean(PurchaseService.class);
        Purchase purchase = new Purchase(PurchaseId.newId(), "PR-1", "Laptop", 1, new BigDecimal("1000.00"));
        service.move(purchase, new Submitted());

        purchase.markManagerApproved();
        service.move(purchase, new Approved());

        purchase.markStockAvailable();
        service.move(purchase, new Ordered());

        purchase.confirmDelivery();
        service.move(purchase, new Completed());

        System.out.println("COMPLETED");
        context.close();
    }
}
