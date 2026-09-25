package purchase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import purchase.domain.PurchasePolicy;
import purchase.domain.rule.ApprovalRule;
import purchase.domain.rule.CompletionRule;
import purchase.domain.rule.DraftRule;
import purchase.domain.rule.OrderRule;
import purchase.domain.rule.PurchaseRule;
import purchase.domain.rule.StatusRule;

import java.util.List;

@Configuration
public class RuleConfig {

    @Bean
    public PurchasePolicy purchasePolicy() {
        return new PurchasePolicy();
    }

    @Bean
    public StatusRule purchaseStatusRule(
            PurchasePolicy purchasePolicy
    ) {
        return new StatusRule(purchasePolicy);
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
    public PurchaseRule purchaseRule(
            StatusRule statusRule,
            DraftRule draftRule,
            ApprovalRule approvalRule,
            OrderRule orderRule,
            CompletionRule completionRule
    ) {
        List<PurchaseRule> rules = List.of(
                statusRule,
                draftRule,
                approvalRule,
                orderRule,
                completionRule
        );

        return (purchase, targetStatus) -> {
            for (PurchaseRule rule : rules) {
                rule.check(purchase, targetStatus);
            }
        };
    }
}