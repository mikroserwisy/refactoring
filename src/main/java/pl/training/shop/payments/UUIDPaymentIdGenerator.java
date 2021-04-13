package pl.training.shop.payments;

import javax.ejb.Singleton;
import java.util.UUID;

@Singleton
public class UUIDPaymentIdGenerator implements PaymentIdGenerator {

    public String getNext() {
        return UUID.randomUUID().toString();
    }

}
