package pl.training.shop.products.ports.output.persistence;

import pl.training.shop.products.domain.Product;

import java.util.List;

public interface ProductQueries {

    List<Product> getAll();

}
