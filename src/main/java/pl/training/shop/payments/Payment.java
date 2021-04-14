package pl.training.shop.payments;

import lombok.*;
import org.javamoney.moneta.FastMoney;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    private String id;
    private FastMoney value;
    @ElementCollection //(fetch = FetchType.EAGER)
    private Map<String, String> properties;
    private Instant timestamp;
    private PaymentStatus status;

    public boolean hasId(String id) {
        return this.id.equals(id);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject)  {
            return true;
        }
        if (!(otherObject instanceof Payment)) {
            return false;
        }
        var otherPayment = (Payment) otherObject;
        return Objects.equals(id, otherPayment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
