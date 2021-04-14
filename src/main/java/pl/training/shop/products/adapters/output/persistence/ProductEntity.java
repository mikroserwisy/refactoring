package pl.training.shop.products.adapters.output.persistence;

import lombok.Data;
import pl.training.shop.products.domain.Product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Data
public class ProductEntity {

    @GeneratedValue
    @Id
    private Long id;
    private boolean promoted;

    @Override
    public boolean equals(Object otherProduct) {
        if (this == otherProduct) {
            return true;
        }
        if (!(otherProduct instanceof Product)) {
            return false;
        }
        var product = (Product) otherProduct;
        return Objects.equals(id, product.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
