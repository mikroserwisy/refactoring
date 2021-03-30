package pl.training.shop.payments;

import java.util.UUID;

public class UUIDPaymentIdGenerator implements PaymentIdGenerator {

    public String getNext() {
        return UUID.randomUUID().toString();
    }

}
