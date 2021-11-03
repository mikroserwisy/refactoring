package pl.sages.platform.tests;

import lombok.Data;

import javax.persistence.*;

@Table(name = "answer_instance")
@Entity
@Data
public class AnswerInstance {

    @GeneratedValue
    @Id
    private Long id;
    @Column(length = 1_000, nullable = false)
    private String text;
    @Column(name = "value")
    private String value;
    @Column(name = "correct_value", nullable = false)
    private String correctValue;

}
