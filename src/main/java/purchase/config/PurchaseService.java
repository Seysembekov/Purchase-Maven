package purchase.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import purchase.domain.DuplicatePurchase;
import purchase.domain.Purchase;
import purchase.domain.PurchaseId;
import purchase.domain.rule.PurchaseRule;
import purchase.domain.status.PurchaseStatus;
import purchase.persistence.PurchaseJdbc;

import java.math.BigDecimal;
import java.sql.SQLException;

@Service
public class PurchaseService {
    private final PurchaseRule rules;
    private final PurchaseJdbc db;
    private final PurchasePort port;

    public PurchaseService(@Qualifier("purchaseRule") PurchaseRule rules, PurchaseJdbc db, PurchasePort port) {
        this.rules = rules;
        this.db = db;
        this.port = port;
    }
    public void move(Purchase purchase, PurchaseStatus targetStatus) {
        rules.check(purchase, targetStatus);
        purchase.changeStatus(targetStatus);
        port.statusChanged(purchase.getId(), targetStatus);
    }
    @Transactional
    public void register(Purchase purchase) {
        try {
            db.insert(purchase);
        } catch (DataIntegrityViolationException e) {
            if (isDuplicate(e)) {throw new DuplicatePurchase(
                    purchase.getBusinessKey(), e
                );
            }
            throw e;}}
    @Transactional
    public void insertTwice(String businessKey) {
        Purchase first = new Purchase(PurchaseId.newId(), businessKey, "Test product", 1, BigDecimal.ONE);
        Purchase second = new Purchase(PurchaseId.newId(), businessKey, "Second product", 1, BigDecimal.TEN);
        db.insert(first);
        try {
            db.insert(second);
        } catch (DataIntegrityViolationException e) {
            if (isDuplicate(e)) {
                throw new DuplicatePurchase(businessKey, e);}
            throw e;}}
    private boolean isDuplicate(Throwable error) {
        Throwable current = error;
        while (current != null) {
            if (current instanceof SQLException sql) {
                return true;
            }
            current = current.getCause();
        }
        return false;}}