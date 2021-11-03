package pl.sages.platform.products;

import lombok.Data;

import java.util.List;

@Data
public class ProductTransferObject {

    private Long id;
    private String name;
    private String description;
    private List<ProductElementTransferObject> elements;

}
