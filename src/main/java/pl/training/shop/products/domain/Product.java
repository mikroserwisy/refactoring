package pl.training.shop.products.domain;

import lombok.Value;

@Value
public class Product {

    Long id;
    String description;
    boolean promoted;

}
