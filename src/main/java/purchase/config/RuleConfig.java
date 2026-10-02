package purchase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import purchase.domain.rule.PurchasePolicy;
import purchase.domain.rule.*;

import java.util.List;

@Configuration
public class RuleConfig {
    @Bean
    public PurchasePolicy purchasePolicy() {
        return new PurchasePolicy();}
    @Bean
    public DraftRule draftValidationRule() {
        return new DraftRule();}
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
    public PurchaseRule purchaseRule(PurchasePolicy purchasePolicy, DraftRule draftRule, ApprovalRule approvalRule, OrderRule orderRule, CompletionRule completionRule) {
        List<PurchaseRule> rules = List.of(purchasePolicy, draftRule, approvalRule, orderRule, completionRule);

        return (purchase, targetStatus) -> {
            for (PurchaseRule rule : rules) {
                rule.check(purchase, targetStatus);
            }
        };
    }
}