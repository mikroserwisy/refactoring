package pl.training.shop.payments.ports.output.persistence;

import pl.training.shop.payments.domain.Payment;

import java.util.Optional;

public interface FindPayment {

    Optional<Payment> findById(String id);

}
