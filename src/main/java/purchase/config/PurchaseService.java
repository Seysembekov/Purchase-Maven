package purchase.config;

import org.springframework.stereotype.Service;
import purchase.domain.Purchase;
import purchase.domain.PurchaseStatus;
import purchase.domain.rule.PurchaseRule;

@Service
public class PurchaseService {

    private final PurchaseRule rules;

    public PurchaseService(PurchaseRule rules) {
        this.rules = rules;
    }

    public void move(
            Purchase purchase,
            PurchaseStatus targetStatus
    ) {
        rules.check(
                purchase,
                targetStatus
        );

        purchase.changeStatus(targetStatus);
    }
}