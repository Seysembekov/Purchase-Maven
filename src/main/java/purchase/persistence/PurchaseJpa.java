package purchase.persistence;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import purchase.domain.Purchase;

import java.util.List;
import java.util.Optional;

@Repository
public class PurchaseJpa {

    private final EntityManager entityManager;
    private final PurchaseMapper mapper;

    public PurchaseJpa(
            EntityManager entityManager,
            PurchaseMapper mapper
    ) {
        this.entityManager = entityManager;
        this.mapper = mapper;
    }

    @Transactional
    public void insert(Purchase purchase) {
        entityManager.persist(
                mapper.toEntity(purchase)
        );
    }

    @Transactional
    public void flush() {
        entityManager.flush();
    }

    @Transactional(readOnly = true)
    public int count(String businessKey) {
        Long result = entityManager.createQuery(
                        """
                        SELECT COUNT(p)
                        FROM PurchaseEntity p
                        WHERE p.businessKey = :businessKey
                        """,
                        Long.class
                )
                .setParameter("businessKey", businessKey)
                .getSingleResult();

        return result.intValue();
    }

    @Transactional(readOnly = true)
    public Optional<Purchase> find(String businessKey) {
        List<PurchaseEntity> result = entityManager.createQuery(
                        """
                        SELECT p
                        FROM PurchaseEntity p
                        WHERE p.businessKey = :businessKey
                        """,
                        PurchaseEntity.class
                )
                .setParameter("businessKey", businessKey)
                .setMaxResults(1)
                .getResultList();

        return result.stream()
                .findFirst()
                .map(mapper::toDomain);
    }
}