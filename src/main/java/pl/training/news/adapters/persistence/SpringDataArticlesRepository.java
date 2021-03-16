package pl.training.news.adapters.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.training.news.domain.Article;
import pl.training.news.domain.ArticlesRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SpringDataArticlesRepository implements ArticlesRepository {

    private final ArticlesJpaRepository repository;
    private final PersistenceMapper persistenceMapper;

    @Override
    public void saveAll(List<Article> articles) {
        var entities = persistenceMapper.toArticleEntities(articles);
        repository.saveAll(entities);
    }

}
