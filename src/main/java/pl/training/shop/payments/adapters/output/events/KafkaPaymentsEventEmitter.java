package pl.training.shop.payments.adapters.output.events;

import pl.training.shop.payments.domain.Payment;
import pl.training.shop.payments.ports.output.events.PaymentsEventEmitter;

public class KafkaPaymentsEventEmitter implements PaymentsEventEmitter {

    @Override
    public void emit(Payment payment) {
        // convert to dto and send to kafka
    }

}
