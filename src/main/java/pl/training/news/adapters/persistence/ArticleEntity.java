package pl.training.news.adapters.persistence;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Table(name = "articles")
@Entity
@Data
public class ArticleEntity {

    @Id
    @Column(length = 1024)
    private String id;
    private String author;
    @Column(length = 1024)
    private String title;
    @Lob
    private String description;
    private LocalDate publicationDate;
    private String source;

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (!(otherObject instanceof ArticleEntity)) {
            return false;
        }
        var articleEntity = (ArticleEntity) otherObject;
        return Objects.equals(id, articleEntity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
