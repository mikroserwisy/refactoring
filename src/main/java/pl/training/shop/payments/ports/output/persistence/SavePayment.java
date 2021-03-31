package pl.training.shop.payments.ports.output.persistence;

import pl.training.shop.payments.domain.Payment;

public interface SavePayment {

    Payment save(Payment payment);

}
