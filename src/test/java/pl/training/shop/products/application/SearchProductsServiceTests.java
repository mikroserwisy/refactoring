package pl.training.shop.products.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.training.shop.products.domain.Product;
import pl.training.shop.products.domain.Score;
import pl.training.shop.products.ports.output.persistence.ProductQueries;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchProductsServiceTests {

    @Mock
    private ProductQueries productQueries;
    private SearchProductsService sut;

    private final Product product = new Product("Some product", false);
    private final Product otherProduct = new Product("Some product and product", false);

    @BeforeEach
    void init() {
        sut = new SearchProductsService(productQueries);
        product.setId(1L);
        otherProduct.setId(2L);
    }

    @Test
    void given_a_product_description_without_keyword_when_search_then_returns_score_with_value_of_zero() {
        when(productQueries.getAll()).thenReturn(List.of(product));
        var actual = sut.findBy("test").get(0);
        assertEquals(new Score(0L, product.getId()), actual);
    }

    @Test
    void given_a_product_description_with_n_keywords_when_search_then_returns_score_with_value_of_n() {
        when(productQueries.getAll()).thenReturn(List.of(product));
        var actual = sut.findBy("product").get(0);
        assertEquals(new Score(1L, product.getId()), actual);
    }

    @Test
    void given_a_products_with_keywords_when_search_then_returns_scores_sorted_by_value_in_descending_order() {
        when(productQueries.getAll()).thenReturn(List.of(product, otherProduct));
        var actual = sut.findBy("product");
        assertEquals(List.of(new Score(2L, 2L), new Score(1L, 1L)), actual);
    }

}
