package pl.training.shop.payments;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class UUIDPaymentIdGenerator implements PaymentIdGenerator {

    public String getNext() {
        return UUID.randomUUID().toString();
    }

}
