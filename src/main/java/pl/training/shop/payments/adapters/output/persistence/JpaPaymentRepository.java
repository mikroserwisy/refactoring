package pl.training.shop.payments.adapters.output.persistence;

import lombok.Setter;
import pl.training.shop.payments.application.PaymentNotFoundException;
import pl.training.shop.payments.domain.Payment;
import pl.training.shop.payments.ports.output.persistence.PaymentQueries;
import pl.training.shop.payments.ports.output.persistence.PaymentUpdates;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

public class JpaPaymentRepository implements PaymentUpdates, PaymentQueries {

    @Inject
    @Setter
    private PersistenceMapper persistenceMapper;
    @PersistenceContext(unitName = "shop")
    private EntityManager entityManager;

    @Override
    public Payment save(Payment payment) {
        var entity = persistenceMapper.toEntity(payment);
        entityManager.persist(entity);
        return persistenceMapper.toDomain(entity);
    }

    @Override
    public Optional<Payment> findById(String id) {
        var entity = entityManager.find(PaymentEntity.class, id);
        if (entity == null) {
            throw new PaymentNotFoundException();
        }
        return Optional.of(persistenceMapper.toDomain(entity));
    }

}
