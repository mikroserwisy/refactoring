package pl.training.shop.payments;

import lombok.Builder;
import lombok.Value;
import org.javamoney.moneta.FastMoney;

import java.time.Instant;
import java.util.Map;

@Builder
@Value
public class Payment {

    String id;
    FastMoney value;
    Map<String, String> properties;
    Instant timestamp;
    PaymentStatus status;

    public boolean hasId(String id) {
        return this.id.equals(id);
    }

}
