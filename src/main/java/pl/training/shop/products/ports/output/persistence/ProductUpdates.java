package pl.training.shop.products.ports.output.persistence;

import pl.training.shop.products.domain.Product;

public interface ProductUpdates {

    Product save(Product product);

}
