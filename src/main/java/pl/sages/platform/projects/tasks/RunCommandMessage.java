package pl.sages.platform.projects.tasks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RunCommandMessage {

    private Long taskId;
    private Long projectId;
    private Path workspacePath;
    private Long commandId;

}
