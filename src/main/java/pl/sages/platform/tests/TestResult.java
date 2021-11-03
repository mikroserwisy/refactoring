package pl.sages.platform.tests;

import lombok.Data;

import java.util.List;

@Data
public class TestResult {

    private boolean passed;
    private int requiredScorePercentage;
    private long timeInSeconds;
    private long numberOfQuestions;
    private long numberOfCorrectQuestions;
    private List<CategoryResult> categoryResults;

}
