package pl.sages.platform.projects;

import lombok.Data;
import pl.sages.platform.projects.commands.CommandTransferObject;

import java.util.Date;
import java.util.Set;

@Data
public class ProjectTransferObject {

    private Long id;
    private String name;
    private String description;
    private String language;
    private Set<ProjectFileTransferObject> files;
    private Set<CommandTransferObject> availableCommands;
    private long importTimestamp;
    private String hint;
    private String solution;

}
