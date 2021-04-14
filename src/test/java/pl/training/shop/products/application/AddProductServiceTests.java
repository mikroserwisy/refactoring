package pl.training.shop.products.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.training.shop.products.domain.Product;
import pl.training.shop.products.ports.output.persistence.ProductUpdates;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddProductServiceTests {

    @Mock
    private ProductUpdates productUpdates;
    private AddProductService sut;

    private final Product product = new Product("Some product", false);

    @BeforeEach
    void init() {
        sut = new AddProductService(productUpdates);
    }

    @Test
    void given_a_new_product_when_add_then_product_is_promoted() {
        when(productUpdates.save(any(Product.class))).then(returnsFirstArg());
        assertTrue(sut.add(product).isPromoted());
    }

}
