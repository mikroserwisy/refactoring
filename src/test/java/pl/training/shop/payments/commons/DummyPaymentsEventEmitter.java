package pl.training.shop.payments.commons;

import pl.training.shop.payments.domain.Payment;
import pl.training.shop.payments.ports.output.events.PaymentsEventEmitter;

public class DummyPaymentsEventEmitter implements PaymentsEventEmitter {

    @Override
    public void emit(Payment payment) {
    }

}
