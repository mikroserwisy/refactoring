package pl.sages.platform.tests;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Table(name = "question_instance")
@Entity
@Data
public class QuestionInstance {

    @GeneratedValue
    @Id
    private Long id;
    private Integer index;
    private String category;
    @Column(length = 1_000, nullable = false)
    private String text;
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;
    private boolean isCorrect;
    @JoinColumn(name = "question_instance_id")
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<AnswerInstance> answers;

}
