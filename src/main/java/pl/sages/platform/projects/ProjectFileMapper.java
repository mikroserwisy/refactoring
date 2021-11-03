package pl.sages.platform.projects;

import lombok.Setter;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = ProjectFileRepository.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ProjectFileMapper {

    @Autowired
    @Setter
    protected ProjectFileRepository projectFileRepository;

    @IterableMapping(elementTargetType = ProjectFileTransferObject.class)
    public abstract Set<ProjectFileTransferObject> toProjectFileTransferObjects(Set<ProjectFile> projectFiles);

    private ProjectFile toProjectFile(ProjectFileTransferObject projectFileTransferObject) {
        ProjectFile projectFile = projectFileRepository.findById(projectFileTransferObject.getId())
                .orElseThrow(IllegalArgumentException::new);
        projectFile.setContent(projectFileTransferObject.getContent());
        return projectFile;
    }

    @IterableMapping(elementTargetType = ProjectFile.class)
    public Set<ProjectFile> toProjectFiles(Set<ProjectFileTransferObject> projectFileTransferObjects) {
        return projectFileTransferObjects.stream()
                .map(this::toProjectFile)
                .collect(Collectors.toSet());
    }

}
