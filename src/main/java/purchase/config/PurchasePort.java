package purchase.config;

import purchase.domain.PurchaseId;
import purchase.domain.status.PurchaseStatus;

public interface PurchasePort {

    void statusChanged(PurchaseId id, PurchaseStatus status);
}