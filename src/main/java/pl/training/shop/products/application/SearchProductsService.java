package pl.training.shop.products.application;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.training.shop.products.domain.Score;
import pl.training.shop.products.ports.input.SearchProductsUseCase;
import pl.training.shop.products.ports.output.persistence.ProductQueries;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@NoArgsConstructor
public class SearchProductsService implements SearchProductsUseCase {

    private final KeywordCounter keywordCounter = new KeywordCounter();

    @Inject
    @NonNull
    private ProductQueries productQueries;

    public List<Score> findBy(String keyword) {
        return productQueries.getAll().stream()
                .map(product -> new Score(keywordCounter.count(product.getDescription(), keyword), product.getId()))
                .sorted()
                .collect(toList());
    }

}
