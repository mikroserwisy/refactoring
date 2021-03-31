package pl.training.shop.payments.ports.output.persistence;

import pl.training.shop.payments.domain.Payment;

public interface PaymentUpdates {

    Payment save(Payment payment);

}
