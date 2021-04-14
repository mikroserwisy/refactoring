package pl.training.shop.products.commons;

import pl.training.shop.products.domain.Product;
import pl.training.shop.products.ports.output.persistence.ProductQueries;

import java.util.List;

import static java.util.Collections.emptyList;

public class ProductQueriesStub implements ProductQueries {

    @Override
    public List<Product> getAll() {
        return emptyList();
    }

}
