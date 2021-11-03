package pl.sages.platform.projects.commands.logs;

import lombok.Data;

import java.util.Date;

@Data
public class LogEntryTransferObject {

    private String value;
    private String commandExecutionStatus;
    private Date timestamp;

}
