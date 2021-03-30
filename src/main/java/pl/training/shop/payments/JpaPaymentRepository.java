package pl.training.shop.payments;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Stateless
public class JpaPaymentRepository implements PaymentRepository {

    @PersistenceContext(unitName = "shop")
    private EntityManager entityManager;

    @Override
    public Payment save(Payment payment) {
        entityManager.persist(payment);
        return payment;
    }

    @Override
    public Optional<Payment> findById(String id) {
        return Optional.ofNullable(entityManager.find(Payment.class, id));
    }

}
