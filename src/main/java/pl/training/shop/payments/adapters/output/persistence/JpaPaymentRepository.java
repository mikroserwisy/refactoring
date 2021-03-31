package pl.training.shop.payments.adapters.output.persistence;

import pl.training.shop.payments.domain.Payment;
import pl.training.shop.payments.ports.output.persistence.FindPayment;
import pl.training.shop.payments.ports.output.persistence.SavePayment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

public class JpaPaymentRepository implements SavePayment, FindPayment {

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
