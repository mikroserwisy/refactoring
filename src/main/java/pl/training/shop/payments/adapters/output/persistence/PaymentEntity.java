package pl.training.shop.payments.adapters.output.persistence;

import lombok.Data;
import org.javamoney.moneta.FastMoney;
import pl.training.shop.payments.domain.Payment;
import pl.training.shop.payments.domain.PaymentStatus;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Entity
@Data
public class PaymentEntity {

    @Id
    private String id;
    private FastMoney value;
    @ElementCollection
    private Map<String, String> additionalProperties;
    private Instant timestamp;
    private String status;

    @Override
    public boolean equals(Object otherPayment) {
        if (this == otherPayment) {
            return true;
        }
        if (!(otherPayment instanceof Payment)) {
            return false;
        }
        var payment = (Payment) otherPayment;
        return Objects.equals(id, id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
