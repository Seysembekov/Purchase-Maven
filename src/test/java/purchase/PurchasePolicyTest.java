package purchase;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PurchasePolicyTest {
    private final PurchasePolicy policy = new PurchasePolicy();
    @Test
    void draftCanBecomeSubmitted() {
        assertTrue(policy.canTransition(new Draft(), new Submitted()));
    }
    @Test
    void submittedCanBecomeApproved() {
        assertTrue(policy.canTransition(new Submitted(), new Approved()));
    }
    @Test
    void approvedCanBecomeOrdered() {
        assertTrue(policy.canTransition(new Approved(), new Ordered()));
    }
    @Test
    void orderedCanBecomeCompleted() {
        assertTrue(policy.canTransition(new Ordered(), new Completed()));
    }
    @Test
    void draftCannotBecomeOrdered() {
        assertFalse(policy.canTransition(new Draft(), new Ordered()));
    }
    @Test
    void completedCannotBecomeOrdered() {
        assertFalse(policy.canTransition(new Completed(), new Ordered()));
    }
}