package pl.sages.platform.products;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class Product implements Serializable {

    @GeneratedValue
    @Id
    private Long id;
    @Column(unique = true)
    private String name;
    @Column(length = 100_000)
    private String description;
    @JoinColumn(name = "product_id")
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ProductElement> elements;

}
