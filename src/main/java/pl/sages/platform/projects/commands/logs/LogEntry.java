package pl.sages.platform.projects.commands.logs;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.sages.platform.projects.commands.CommandExecutionStatus;

import javax.persistence.*;
import java.util.Date;

@Table(indexes = {
        @Index(name = "log_entry_project_path_index",  columnList="project_path")
})
@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class LogEntry {

    @GeneratedValue
    @Id
    private Long id;
    @Column(name = "project_path")
    @NonNull
    private String projectPath;
    @Column(length = 100_000)
    @NonNull
    private String value;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "command_execution_status")
    private CommandExecutionStatus commandExecutionStatus;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date timestamp;

}
