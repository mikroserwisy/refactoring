package pl.sages.platform.tests;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Answer {

    @GeneratedValue
    @Id
    private Long id;
    @Column(length = 1_000, nullable = false)
    private String text;
    @Column(name = "correct_value", nullable = false)
    private String correctValue;

}
