package pl.sages.platform.projects;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pl.sages.platform.accounts.AccountService;
import pl.sages.platform.common.configuration.TransactionalService;
import pl.sages.platform.common.pagination.ResultPage;
import pl.sages.platform.products.ProductElementService;
import pl.sages.platform.repositories.Repository;
import pl.sages.platform.repositories.RepositoryService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@TransactionalService
@RequiredArgsConstructor
public class ProjectService {

    private static final String RELATIVE_METADATA_PATH = "metadata/project.json";
    private static final String REPOSITORIES_FOLDER = "repositories";
    private static final String AUTHOR_FILE = "user.txt";

    @NonNull
    private final ProductElementService productElementService;
    @NonNull
    private final RepositoryService repositoryService;
    @NonNull
    private final ProjectRepository projectRepository;
    @NonNull
    private final AccountService accountService;
    @NonNull
    private final ObjectMapper objectMapper;
    @Value("${platform.storage-path}")
    @Setter
    private Path storagePath;

    public Project getProject(Long projectId) {
        return projectRepository.getById(projectId)
                .orElseThrow(ProjectNotFoundException::new);
    }

    public ResultPage<Project> getProjects(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Project> projectsPage = projectRepository.findAll(pageRequest);
        return new ResultPage<>(projectsPage.getContent(), pageNumber, projectsPage.getTotalPages());
    }

    public Project createProjectInWorkspace(Long projectId, Path workspacePath) {
        Project project = getProject(projectId);
        Path projectPath = getProjectPath(workspacePath, projectId);
        if (Files.notExists(projectPath)) {
            String remoteUrl = storagePath.resolve(REPOSITORIES_FOLDER).resolve(project.getRepositoryUrl()).toString();
            Repository repository = new Repository(remoteUrl);
            repository.setLocalPath(projectPath);
            repositoryService.cloneRepository(repository);
        }
        return project;
    }

    public void deleteProjectFromWorkspace(Long projectId, Path workspacePath) {
        Path projectPath = getProjectPath(workspacePath, projectId);
        repositoryService.deleteRepository(projectPath);
    }

    public Set<ProjectFile> getFilesFromWorkspace(Long projectId, Path workspacePath) {
        Path projectPath = getProjectPath(workspacePath, projectId);
        if (Files.notExists(projectPath)) {
            throw new ProjectNotFoundException();
        }
        Set<ProjectFile> projectFiles = getProject(projectId).getFiles();
        projectFiles.forEach(projectFile -> readFileContent(projectPath.resolve(projectFile.getFullPath()))
                .ifPresent(projectFile::setContent));
        return projectFiles;
    }

    public void updateFilesInWorkspace(Long projectId, Path workspacePath, Set<ProjectFile> projectFiles) {
        Path projectPath = getProjectPath(workspacePath, projectId);
        if (Files.notExists(projectPath)) {
            throw new ProjectNotFoundException();
        }
        projectFiles.stream()
                .filter(ProjectFile::isEditable)
                .forEach(projectFile -> writeFileContent(projectPath.resolve(projectFile.getFullPath()), projectFile.getContent()));
        writeFileContent(workspacePath.resolve(AUTHOR_FILE), accountService.getActiveAccount().toString());
        Repository repository = new Repository();
        repository.setLocalPath(projectPath);
        repositoryService.commitChanges(repository, Instant.now().toString());
    }

    public Path getProjectPath(Path workspacePath, Long projectId) {
        return workspacePath.resolve(projectId.toString());
    }

    private void writeFileContent(Path path, String content) {
        try {
            Files.write(path, content.getBytes());
        } catch (IOException e) {
            throw new WriteFileFailedException();
        }
    }

    private Optional<String> readFileContent(Path path) {
        try {
            return Optional.of(Files.readString(path));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public void importProject(Repository repository) {
        try {
            Path relativeLocalPath = createLocalPath(repository.getRemoteUrl());
            Path absoluteLocalPath = storagePath.resolve(REPOSITORIES_FOLDER).resolve(relativeLocalPath);
            repository.setLocalPath(absoluteLocalPath);
            if (Files.notExists(absoluteLocalPath)) {
                repositoryService.cloneRepository(repository);
            } else {
                repositoryService.pullChanges(repository);
            }
            importProjectMetadata(relativeLocalPath, absoluteLocalPath);
        } catch (IOException e) {
            repositoryService.deleteRepository(repository.getLocalPath());
            throw new InvalidProjectMetadataException();
        }
    }

    private Path createLocalPath(String remoteUrl) throws MalformedURLException {
        URL url = new URL(remoteUrl);
        return Path.of(url.getHost(), url.getPath());
    }

    private void importProjectMetadata(Path relativeLocalPath, Path absoluteLocalPath) throws IOException {
        Path metadataPath = absoluteLocalPath.resolve(RELATIVE_METADATA_PATH);
        Project project = objectMapper.readValue(metadataPath.toFile(), Project.class);
        Optional<Project> oldProject = projectRepository.getByRepositoryUrl(relativeLocalPath.toString());
        project.setRepositoryUrl(relativeLocalPath.toString());
        project.setImportTimestamp(System.currentTimeMillis());
        projectRepository.saveAndFlush(project);
        if (oldProject.isPresent()) {
            productElementService.updateElementId(oldProject.get().getId(), project.getId());
            projectRepository.delete(oldProject.get());
        }
    }

    public void deleteProject(Long projectId) {
        productElementService.isElementSaveToDelete(projectId);
        Project project = getProject(projectId);
        projectRepository.delete(project);
        Path repositoryPath = storagePath.resolve(REPOSITORIES_FOLDER).resolve(project.getRepositoryUrl());
        repositoryService.deleteRepository(repositoryPath);
    }

}
