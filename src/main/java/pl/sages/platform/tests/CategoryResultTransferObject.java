package pl.sages.platform.tests;

import lombok.Data;

@Data
public class CategoryResultTransferObject {

    private String name;
    private long numberOfQuestions;
    private long numberOfCorrectQuestions;

}
