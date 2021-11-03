package pl.sages.platform.tests;

import lombok.Data;

import java.util.List;

@Data
public class TestResultTransferObject {

    private boolean passed;
    private int requiredScorePercentage;
    private long timeInSeconds;
    private long numberOfQuestions;
    private long numberOfCorrectQuestions;
    private List<CategoryResultTransferObject> categoryResults;

}
