package pl.training.shop.payments;

import lombok.*;
import org.javamoney.moneta.FastMoney;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;
import java.util.Map;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    private String id;
    private FastMoney value;
    @ElementCollection
    private Map<String, String> properties;
    private Instant timestamp;
    private PaymentStatus status;

    public boolean hasId(String id) {
        return this.id.equals(id);
    }

}
