package pl.sages.platform.projects;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sages.platform.accounts.Account;
import pl.sages.platform.accounts.AccountService;
import pl.sages.platform.common.ResourceUri;
import pl.sages.platform.common.exception.ExceptionResponseBuilder;
import pl.sages.platform.common.pagination.ResultPage;
import pl.sages.platform.common.pagination.ResultPageTransferObject;
import pl.sages.platform.products.ElementInUseException;
import pl.sages.platform.projects.tasks.Task;
import pl.sages.platform.projects.tasks.TaskService;
import pl.sages.platform.repositories.CloneRepositoryFailedException;
import pl.sages.platform.repositories.Repository;
import pl.sages.platform.repositories.RepositoryMapper;
import pl.sages.platform.repositories.RepositoryTransferObject;

import java.net.URI;
import java.util.Locale;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;

@RequestMapping("${platform.api-prefix}")
@RestController
@RequiredArgsConstructor
public class ProjectRestController {

    private static final String TASKS_PATH = "tasks";

    @NonNull
    private final ProjectService projectService;
    @NonNull
    private final TaskService taskService;
    @NonNull
    private final AccountService accountService;
    @NonNull
    private final ProjectMapper projectMapper;
    @NonNull
    private final ProjectFileMapper projectFileMapper;
    @NonNull
    private final RepositoryMapper repositoryMapper;
    @NonNull
    private final WorkspacePathResolver workspacePathResolver;
    @NonNull
    private final ResourceUri resourceUri;
    @NonNull
    private final ExceptionResponseBuilder exceptionResponseBuilder;

    @RequestMapping(value = "projects", method = RequestMethod.GET)
    public ResponseEntity<ResultPageTransferObject<ProjectSummaryTransferObject>> getProjectsSummaries(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "50") int pageSize) {
        ResultPage<Project> projectsPage = projectService.getProjects(pageNumber, pageSize);
        ResultPageTransferObject<ProjectSummaryTransferObject> resultPageTransferObject = projectMapper.toProjectSummaryTransferObjects(projectsPage);
        return ResponseEntity.ok(resultPageTransferObject);
    }

    @RequestMapping(value = "projects/{projectId}", method = RequestMethod.GET)
    public ResponseEntity<ProjectTransferObject> getProjectDetails(@PathVariable("projectId") Long projectId) {
        Project project = projectService.getProject(projectId);
        ProjectTransferObject projectTransferObject = projectMapper.toProjectTransferObject(project);
        return ResponseEntity.ok(projectTransferObject);
    }

    @RequestMapping(value = "workspace/projects/{projectId}", method = RequestMethod.PUT)
    public ResponseEntity<Void> createProjectInWorkspace(@PathVariable("projectId") Long projectId) {
        Task task = taskService.initProject(projectId, workspacePathResolver.getPath(), getAccountId());
        URI taskUri = resourceUri.from(TASKS_PATH, task.getId());
        return ResponseEntity.accepted().location(taskUri).build();
    }

    @RequestMapping(value = "workspace/projects/{projectId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteProjectFromWorkspace(@PathVariable("projectId") Long projectId) {
        projectService.deleteProjectFromWorkspace(projectId, workspacePathResolver.getPath());
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "workspace/projects/{projectId}/files", method = RequestMethod.GET)
    public ResponseEntity<Set<ProjectFileTransferObject>> getFilesFromWorkspace(@PathVariable("projectId") Long projectId) {
        Set<ProjectFile>  projectFiles = projectService.getFilesFromWorkspace(projectId, workspacePathResolver.getPath());
        Set<ProjectFileTransferObject> projectTransferObjects = projectFileMapper.toProjectFileTransferObjects(projectFiles);
        return ResponseEntity.ok(projectTransferObjects);
    }

    @RequestMapping(value = "workspace/projects/{projectId}/files", method = RequestMethod.PATCH)
    public ResponseEntity<Void> updateFilesInWorkspace(@PathVariable("projectId") Long projectId, @RequestBody Set<ProjectFileTransferObject> projectFileTransferObjects) {
        Set<ProjectFile> projectFiles = projectFileMapper.toProjectFiles(projectFileTransferObjects);
        projectService.updateFilesInWorkspace(projectId, workspacePathResolver.getPath(), projectFiles);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value ="repositories/projects", method = RequestMethod.POST)
    public ResponseEntity<Void> importProject(@RequestBody RepositoryTransferObject repositoryTransferObject) {
        Repository repository = repositoryMapper.toRepository(repositoryTransferObject);
        projectService.importProject(repository);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "projects/{projectId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteProject(@PathVariable("projectId") Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    private Long getAccountId() {
        Account account = accountService.getActiveAccount();
        return account.getId();
    }

    @ExceptionHandler(WriteFileFailedException.class)
    public ResponseEntity onWriteFileFailedException(WriteFileFailedException exception, Locale locale) {
        return exceptionResponseBuilder.buildResponse(exception, INTERNAL_SERVER_ERROR, locale);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity onProjectNotFoundException(ProjectNotFoundException exception, Locale locale) {
        return exceptionResponseBuilder.buildResponse(exception, NOT_FOUND, locale);
    }

    @ExceptionHandler(ElementInUseException.class)
    public ResponseEntity onProductElementInUseException(ElementInUseException exception, Locale locale) {
        return exceptionResponseBuilder.buildResponse(exception, BAD_REQUEST, locale);
    }

    @ExceptionHandler(InvalidProjectMetadataException.class)
    public ResponseEntity onProjectNotFoundException(InvalidProjectMetadataException exception, Locale locale) {
        return exceptionResponseBuilder.buildResponse(exception, BAD_REQUEST, locale);
    }

    @ExceptionHandler(CloneRepositoryFailedException.class)
    public ResponseEntity onCloneRepositoryFailedException(CloneRepositoryFailedException exception, Locale locale) {
        return exceptionResponseBuilder.buildResponse(exception, INTERNAL_SERVER_ERROR, locale);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity onIllegalArgumentException(IllegalArgumentException exception, Locale locale) {
        return exceptionResponseBuilder.buildResponse(exception, NOT_FOUND, locale);
    }

}
