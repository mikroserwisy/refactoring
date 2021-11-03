package pl.sages.platform.products;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Table(indexes = {
        @Index(name = "product_element_type_index",  columnList="type"),
        @Index(name = "product_element_element_id_index",  columnList="element_id")
})
@Entity
@Data
public class ProductElement implements Serializable {

    @GeneratedValue
    @Id
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductElementType type;
    @Column(name = "element_id", nullable = false)
    private Long elementId;

}
