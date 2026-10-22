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
import purchase.persistence.PurchaseJpa;

import java.math.BigDecimal;
import java.sql.SQLException;

@Service
public class PurchaseService {

    private final PurchaseRule rules;
    private final PurchaseJpa db;
    private final PurchasePort port;

    public PurchaseService(
            @Qualifier("purchaseRule") PurchaseRule rules,
            PurchaseJpa db,
            PurchasePort port
    ) {
        this.rules = rules;
        this.db = db;
        this.port = port;
    }

    public void move(
            Purchase purchase,
            PurchaseStatus targetStatus
    ) {
        if (purchase == null || targetStatus == null) {
            throw new IllegalArgumentException(
                    "Purchase and target status are required"
            );
        }

        rules.check(purchase, targetStatus);

        purchase.changeStatus(targetStatus);

        port.statusChanged(
                purchase.getId(),
                targetStatus
        );
    }

    @Transactional
    public void register(Purchase purchase) {
        try {
            db.insert(purchase);

            // Force the INSERT now so the exception
            // can be translated inside this method.
            db.flush();

        } catch (DataIntegrityViolationException ex) {
            if (isUniqueConstraintViolation(ex)) {
                throw new DuplicatePurchase(
                        purchase.getBusinessKey(),
                        ex
                );
            }

            throw ex;
        }
    }

    @Transactional
    public void insertTwice(String businessKey) {
        Purchase first = new Purchase(
                PurchaseId.newId(),
                businessKey,
                "Test product",
                1,
                BigDecimal.ONE
        );

        Purchase second = new Purchase(
                PurchaseId.newId(),
                businessKey,
                "Second product",
                1,
                BigDecimal.TEN
        );

        db.insert(first);

        try {
            // Different UUID, same business key.
            db.insert(second);

            // Force both pending INSERT statements to execute.
            db.flush();

        } catch (DataIntegrityViolationException ex) {
            if (isUniqueConstraintViolation(ex)) {
                throw new DuplicatePurchase(
                        businessKey,
                        ex
                );
            }

            throw ex;
        }
    }

    private boolean isUniqueConstraintViolation(
            Throwable error
    ) {
        Throwable current = error;

        while (current != null) {
            if (current instanceof SQLException sqlException
                    && "23505".equals(
                    sqlException.getSQLState()
            )) {
                return true;
            }

            current = current.getCause();
        }

        return false;
    }
}