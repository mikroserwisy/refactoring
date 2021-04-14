package pl.training.shop.products.application;

import pl.training.shop.products.domain.Product;
import pl.training.shop.products.ports.input.AddProductUseCase;

public class AddProductService implements AddProductUseCase {

    @Override
    public Product add(Product product) {
        return new Product(true);
    }

}
