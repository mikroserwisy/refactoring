package pl.sages.platform.projects.commands;

import lombok.Data;

@Data
public class CommandTransferObject {

    private Long id;
    private String name;
    private String expectedResult;

}
