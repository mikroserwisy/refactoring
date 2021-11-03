package pl.sages.platform.tests;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Table(indexes = {
        @Index(name = "test_repository_url_index",  columnList="repository_url")
})
@Entity
@Data
public class Test {

    @GeneratedValue
    @Id
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(length = 100_000)
    private String description;
    @Column(name = "repository_url", nullable = false)
    private String repositoryUrl;
    @Column(name = "time_limit")
    private long timeLimitSeconds;
    @Column(name = "required_score_percentage")
    private int requiredScorePercentage;
    @JoinColumn(name = "test_id")
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private Set<Question> questions;
    private long importTimestamp;

}
