package pl.training.news.adapters.persistence;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.training.news.domain.Article;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface PersistenceMapper {

    @Mapping(source = "url", target = "id")
    ArticleEntity toArticleEntity(Article article);

    List<ArticleEntity> toArticleEntities(List<Article> articles);

}
