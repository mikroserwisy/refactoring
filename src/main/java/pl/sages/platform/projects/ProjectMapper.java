package pl.sages.platform.projects;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.sages.platform.common.pagination.ResultPage;
import pl.sages.platform.common.pagination.ResultPageTransferObject;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectMapper {

    ProjectTransferObject toProjectTransferObject(Project project);

    @IterableMapping(elementTargetType = ProjectSummaryTransferObject.class)
    List<ProjectSummaryTransferObject> toProjectSummaryTransferObjects(List<Project> projects);

    ResultPageTransferObject<ProjectSummaryTransferObject> toProjectSummaryTransferObjects(ResultPage<Project> projectResultPage);

}
