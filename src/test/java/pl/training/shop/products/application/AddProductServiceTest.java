package pl.training.shop.products.application;

import org.junit.jupiter.api.Test;
import pl.training.shop.products.domain.Product;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AddProductServiceTest {

    @Test
    void given_a_new_product_when_add_then_product_is_promoted() {
        var sut = new AddProductService();
        var product = sut.add(new Product(false));
        assertTrue(product.isPromoted());
    }

}
