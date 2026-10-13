package purchase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import purchase.domain.rule.*;

import java.util.List;

@Configuration
public class RuleConfig {

    @Bean
    public PurchasePolicy purchasePolicy() {
        return new PurchasePolicy();
    }

    @Bean
    public DraftRule draftValidationRule() {
        return new DraftRule();
    }

    @Bean
    public ApprovalRule approvalRule() {
        return new ApprovalRule();
    }

    @Bean
    public OrderRule orderRule() {
        return new OrderRule();
    }

    @Bean
    public CompletionRule completionRule() {
        return new CompletionRule();
    }

    @Bean
    public PurchasePort purchasePort() {
        return (id, status) ->
                System.out.println(
                        "Purchase " + id.value()
                                + " changed to "
                                + status.getClass().getSimpleName()
                );
    }

    @Bean
    public PurchaseRule purchaseRule(
            PurchasePolicy policy,
            DraftRule draft,
            ApprovalRule approval,
            OrderRule order,
            CompletionRule completion
    ) {
        List<PurchaseRule> rules = List.of(
                policy, draft, approval, order, completion
        );

        return (purchase, targetStatus) -> {
            for (PurchaseRule rule : rules) {
                rule.check(purchase, targetStatus);
            }
        };
    }
}