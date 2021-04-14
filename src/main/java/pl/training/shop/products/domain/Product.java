package pl.training.shop.products.domain;

import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Product {

    private Long id;
    @NonNull
    private String description;
    @NonNull
    private boolean promoted;

}
