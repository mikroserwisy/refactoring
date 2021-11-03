package pl.sages.platform.projects.tasks;

import lombok.Data;

import java.util.Date;

@Data
public class TaskTransferObject {

    private TaskStatus status;
    private Date startedAt;
    private Date finishedAt;

}
