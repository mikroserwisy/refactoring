package pl.sages.platform.projects.commands;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ExecCreation;
import com.spotify.docker.client.messages.HostConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import pl.sages.platform.common.configuration.TransactionalService;
import pl.sages.platform.projects.Project;
import pl.sages.platform.projects.commands.logs.LogEntry;
import pl.sages.platform.projects.commands.logs.LogService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import static com.spotify.docker.client.DockerClient.ExecCreateParam;

@TransactionalService
@Slf4j
@RequiredArgsConstructor
public class CommandService {

    private static final Path CONTAINER_WORKING_DIR = Paths.get("/sandbox");
    private static final Path CONTAINER_MAVEN_REPOSITORY = Paths.get("/root/.m2");
    private static final String PATH_SEPARATOR = ":";
    private static final long CPU_QUOTA = 500_000L;

    @NonNull
    private final LogService logService;
    @Value("${platform.volume-mount-prefix}")
    @Setter
    private String volumeMountPrefix;
    @Value("${platform.maven-repository-path}")
    @Setter
    private Path localMavenRepository;
    @Value("${platform.docker-host-uri}")
    @Setter
    private URI dockerHostUri;
    private DockerClient dockerClient;

    @PostConstruct
    public void init() {
        dockerClient = DefaultDockerClient.builder().uri(dockerHostUri).build();
    }

    public void runCommand(Project project, Path projectPath, Long commandId) {
        Command command = project.getCommand(commandId);
        HostConfig hostConfig = prepareHostConfig(projectPath);
        ContainerConfig containerConfig = prepareContainerConfig(hostConfig, project.getRuntimeEnvironment());
        logService.deleteLogEntries(projectPath);
        runCommand(projectPath, command, containerConfig);
    }

    private HostConfig prepareHostConfig(Path projectPath) {
        return HostConfig.builder()
                .cpuQuota(CPU_QUOTA)
                .appendBinds(bindPaths(projectPath, CONTAINER_WORKING_DIR))
                .appendBinds(bindPaths(localMavenRepository, CONTAINER_MAVEN_REPOSITORY))
                .build();
    }

    private String bindPaths(Path localPath, Path containerPath) {
        return volumeMountPrefix + localPath + PATH_SEPARATOR + containerPath;
    }

    private ContainerConfig prepareContainerConfig(HostConfig hostConfig, String runtimeName) {
        return ContainerConfig.builder()
                .image(runtimeName)
                .hostConfig(hostConfig)
                .build();
    }

    private void runCommand(Path projectPath, Command command, ContainerConfig containerConfig) {
        try {
            ContainerCreation container = runContainer(containerConfig);
            runInstructions(projectPath, command, container);
            destroyContainer(container.id());
        } catch (DockerException | InterruptedException exception) {
            log.error("Run command failed", exception);
            throw new RunCommandFailedException();
        }
    }

    private ContainerCreation runContainer(ContainerConfig containerConfig) throws DockerException, InterruptedException {
        ContainerCreation container = dockerClient.createContainer(containerConfig);
        dockerClient.startContainer(container.id());
        return container;
    }

    private void runInstructions(Path projectPath, Command command, ContainerCreation container) throws DockerException, InterruptedException {
        for (String[] instruction : command.parse()) {
            String commandOutput = runInstruction(container.id(), instruction);
            addLogEntry(projectPath, command, commandOutput);
        }
    }

    private String runInstruction(String containerId, String[] instruction) throws DockerException, InterruptedException {
        ExecCreation execCreation = dockerClient.execCreate(containerId, instruction, ExecCreateParam.attachStdout(), ExecCreateParam.attachStderr());
        LogStream output = dockerClient.execStart(execCreation.id());
        return output.readFully();
    }

    private void addLogEntry(Path projectPath, Command command, String commandOutput) {
        LogEntry logEntry = new LogEntry(projectPath.toString(), commandOutput);
        logEntry.setCommandExecutionStatus(command.getStatus(commandOutput));
        logEntry.setTimestamp(new Date());
        logService.addLogEntry(logEntry);
    }

    private void destroyContainer(String id) throws DockerException, InterruptedException {
        dockerClient.killContainer(id);
        dockerClient.removeContainer(id);
    }

    @PreDestroy
    public void onClose() {
        dockerClient.close();
    }

}
