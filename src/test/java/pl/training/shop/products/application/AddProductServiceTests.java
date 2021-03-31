package pl.training.shop.products.application;

import org.junit.jupiter.api.Test;
import pl.training.shop.products.domain.Product;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AddProductServiceTests {

    private final AddProductService sut = new AddProductService();

    @Test
    void given_new_product_when_add_then_product_is_promoted() {
        var product = sut.add(new Product());
        assertTrue(product.isPromoted());
    }

}
