package pl.training.shop.products.ports.input;

import pl.training.shop.products.domain.Score;

import java.util.List;

public interface SearchProductsUseCase {

    List<Score> findBy(String keyword);

}
