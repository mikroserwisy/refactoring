package pl.training.shop.payments.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.javamoney.moneta.FastMoney;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    private String id;
    private FastMoney value;
    private Map<String, String> properties;
    private Instant timestamp;
    private PaymentStatus status;

    public boolean hasId(String id) {
        return this.id.equals(id);
    }

}
