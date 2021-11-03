package pl.sages.platform.tests;

import lombok.Data;

import java.util.Set;

@Data
public class QuestionInstanceTransferObject {

    private Long id;
    private Integer index;
    private String category;
    private String text;
    private QuestionType questionType;
    private Set<AnswerInstanceTransferObject> answers;

}
