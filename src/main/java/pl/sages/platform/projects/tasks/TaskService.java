package pl.sages.platform.projects.tasks;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.sages.platform.projects.Project;
import pl.sages.platform.projects.ProjectService;
import pl.sages.platform.projects.commands.CommandService;

import java.nio.file.Path;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {

    private static final int UNFINISHED_TASKS_LIMIT = 10;

    private Semaphore semaphore = new Semaphore(1, true);
    @NonNull
    private final TaskRepository taskRepository;
    @NonNull
    private final ProjectService projectService;
    @NonNull
    private final CommandService commandService;
    @NonNull
    private final Queue initProjectQueue;
    @NonNull
    private final Queue runCommandQueue;
    @NonNull
    private final RabbitTemplate jmsTemplate;

    public Task initProject(Long projectId, Path workspacePath, Long ownerId) {
        Task task = createTask(projectId, ownerId);
        semaphore.release();
        InitProjectMessage initProjectMessage = new InitProjectMessage(task.getId(), projectId, workspacePath);
        jmsTemplate.convertAndSend(initProjectQueue.getName(), initProjectMessage);
        return task;
    }

    @RabbitListener(queues = "${platform.init-project-queue}")
    public void onInitProject(InitProjectMessage initProjectMessage) {
        Task task = getTask(initProjectMessage.getTaskId());
        processTask(task, () -> projectService.createProjectInWorkspace(initProjectMessage.getProjectId(), initProjectMessage.getWorkspacePath()));
    }

    public Task runCommand(Long projectId, Path workspacePath, Long commandId, Long ownerId) {
        Task task = createTask(projectId, ownerId);
        semaphore.release();
        RunCommandMessage runCommandMessage = new RunCommandMessage(task.getId(), projectId, workspacePath, commandId);
        jmsTemplate.convertAndSend(runCommandQueue.getName(), runCommandMessage);
        return task;
    }

    @RabbitListener(queues = "${platform.run-command-queue}")
    public void onRunCommandMessage(RunCommandMessage runCommandMessage) {
        Project project = projectService.getProject(runCommandMessage.getProjectId());
        Task task = getTask(runCommandMessage.getTaskId());
        Path projectPath = projectService.getProjectPath(runCommandMessage.getWorkspacePath(), project.getId());
        processTask(task, () -> commandService.runCommand(project, projectPath , runCommandMessage.getCommandId()));
    }

    public Task getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(TaskNotFoundException::new);
    }

    private void processTask(Task task, Runnable runnable) {
        markTaskAsStarted(task);
        try {
            runnable.run();
            markTaskAsFinished(task);
        } catch (Exception exception) {
            log.info("Task failed: " + exception.getClass().getSimpleName());
            markTaskAsFailed(task);
        }
    }

    private Task createTask(Long projectId, Long ownerId) {
        verifyUnfinishedTasksLimit(ownerId);
        Task task = new Task(projectId, ownerId);
        task.setStatus(TaskStatus.PENDING);
        return taskRepository.saveAndFlush(task);
    }

    private void verifyUnfinishedTasksLimit(Long userId) {
        try {
            if (!semaphore.tryAcquire(1, TimeUnit.SECONDS)) {
                throw new TaskNotAcceptedException();
            }
            if (taskRepository.getNotFinishedTasksCount(userId) >= UNFINISHED_TASKS_LIMIT) {
                semaphore.release();
                throw new TaskNotAcceptedException();
            }
        } catch (InterruptedException ex) {
            throw new TaskNotAcceptedException();
        }
    }

    private void markTaskAsStarted(Task task) {
        task.setStatus(TaskStatus.RUNNING);
        task.setStartedAt(new Date());
        taskRepository.saveAndFlush(task);
    }

    private void markTaskAsFinished(Task task) {
        task.setStatus(TaskStatus.FINISHED);
        task.setFinishedAt(new Date());
        taskRepository.saveAndFlush(task);
    }

    private void markTaskAsFailed(Task task) {
        task.setStatus(TaskStatus.FAILED);
        task.setFinishedAt(new Date());
        taskRepository.saveAndFlush(task);
    }

    @Scheduled(fixedRate = 1_000 * 3_600)
    public void deleteOldTasks() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -24);
        log.info("Deleting old tasks");
        taskRepository.deleteOldTasks(calendar.getTime());
    }

}
