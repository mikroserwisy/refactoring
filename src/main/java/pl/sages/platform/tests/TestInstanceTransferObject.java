package pl.sages.platform.tests;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class TestInstanceTransferObject {

    private Long id;
    private Date startTime;
    private Date lastUpdateTime;
    private int currentQuestionIndex;
    private Set<QuestionInstanceTransferObject> questions;

}
