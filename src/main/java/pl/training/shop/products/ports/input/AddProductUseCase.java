package pl.training.shop.products.ports.input;

import pl.training.shop.products.domain.Product;

public interface AddProductUseCase {

    Product add(Product product);

}
