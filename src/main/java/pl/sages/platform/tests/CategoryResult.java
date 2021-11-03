package pl.sages.platform.tests;

import lombok.Data;

@Data
public class CategoryResult {

    private String name;
    private long numberOfQuestions;
    private long numberOfCorrectQuestions;

}
