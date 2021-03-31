package pl.training.shop.payments.ports.output.events;

import pl.training.shop.payments.domain.Payment;

public interface PaymentsEventEmitter {

    void emit(Payment payment);

}
