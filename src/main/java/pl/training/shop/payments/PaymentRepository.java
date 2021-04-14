package pl.training.shop.payments;

import javax.ejb.Local;
import java.util.Optional;

@Local
public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findById(String id);

}
