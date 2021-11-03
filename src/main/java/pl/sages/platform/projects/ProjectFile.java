package pl.sages.platform.projects;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@Entity
@Data
@NoArgsConstructor
public class ProjectFile {

    @GeneratedValue
    @Id
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String path;
    @Column(nullable = false)
    private boolean editable;
    @Transient
    private String content;

    public Path getFullPath() {
        return Paths.get(path, name);
    }

}
