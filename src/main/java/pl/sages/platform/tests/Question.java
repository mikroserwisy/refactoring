package pl.sages.platform.tests;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class Question {

    @GeneratedValue
    @Id
    private Long id;
    private Integer index;
    private String category;
    @Column(length = 1_000, nullable = false)
    private String text;
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;
    @JoinColumn(name = "question_id")
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private Set<Answer> answers;

}
