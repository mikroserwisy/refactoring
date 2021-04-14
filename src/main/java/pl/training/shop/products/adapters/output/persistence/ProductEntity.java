package pl.training.shop.products.adapters.output.persistence;

import lombok.Data;
import pl.training.shop.products.domain.Product;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
public class ProductEntity {

    @GeneratedValue
    @Id
    private Long id;
    private boolean promoted;
    @Lob
    @Column(length = 2048)
    private String description;

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
