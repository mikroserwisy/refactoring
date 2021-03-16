package pl.training.news.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticlesJpaRepository extends JpaRepository<ArticleEntity, Long> {
}
