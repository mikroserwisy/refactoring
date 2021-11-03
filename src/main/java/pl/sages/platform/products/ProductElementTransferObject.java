package pl.sages.platform.products;

import lombok.Data;

@Data
public class ProductElementTransferObject {

    private Long id;
    private ProductElementType type;
    private Long elementId;

}
