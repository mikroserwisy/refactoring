package pl.sages.platform.projects;

import lombok.Data;
import pl.sages.platform.projects.commands.Command;
import pl.sages.platform.projects.commands.CommandNotFoundException;

import javax.persistence.*;
import java.util.Set;

@Table(indexes = {
        @Index(name = "project_repository_url_index",  columnList="repository_url")
})
@Entity
@Data
public class Project {

    @GeneratedValue
    @Id
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(length = 100_000)
    private String description;
    @Column(name = "repository_url", nullable = false)
    private String repositoryUrl;
    @JoinColumn(name = "project_id")
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private Set<ProjectFile> files;
    @Column(name = "runtime_environment", nullable = false)
    private String runtimeEnvironment;
    @Column(nullable = false)
    private String language;
    @JoinColumn(name = "project_id")
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private Set<Command> availableCommands;
    private long importTimestamp;
    @Column(length = 100_000)
    private String hint;
    @Column(length = 100_000)
    private String solution;

    public Command getCommand(Long commandId) {
        return availableCommands.stream()
                .filter(command -> command.hasId(commandId))
                .findFirst()
                .orElseThrow(CommandNotFoundException::new);
    }

}
