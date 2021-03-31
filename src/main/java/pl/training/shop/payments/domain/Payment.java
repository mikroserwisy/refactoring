package pl.training.shop.payments.domain;

import lombok.*;
import org.javamoney.moneta.FastMoney;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Entity
@Data
@Builder
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

    @Override
    public boolean equals(Object otherPayment) {
        if (this == otherPayment) {
            return true;
        }
        if (!(otherPayment instanceof  Payment)) {
            return false;
        }
        var payment = (Payment) otherPayment;
        return Objects.equals(id, payment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
