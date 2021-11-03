package pl.sages.platform.projects.tasks;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Table(indexes = {
        @Index(name = "task_status_index",  columnList="status"),
        @Index(name = "task_started_at_index",  columnList="started_at")
})
@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Task {

    @GeneratedValue
    @Id
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "started_at")
    private Date startedAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "finished_at")
    private Date finishedAt;
    @Column(nullable = false)
    @NonNull
    private Long projectId;
    @Column(nullable = false)
    @NonNull
    private Long ownerId;

}
