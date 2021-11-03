package pl.sages.platform.projects.tasks;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sages.platform.accounts.Account;
import pl.sages.platform.accounts.AccountService;
import pl.sages.platform.common.ResourceUri;
import pl.sages.platform.common.exception.ExceptionResponseBuilder;
import pl.sages.platform.projects.WorkspacePathResolver;
import pl.sages.platform.projects.commands.logs.LogEntry;
import pl.sages.platform.projects.commands.logs.LogEntryMapper;
import pl.sages.platform.projects.commands.logs.LogEntryTransferObject;
import pl.sages.platform.projects.commands.logs.LogService;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequestMapping("${platform.api-prefix}")
@RestController
@RequiredArgsConstructor
public class TaskRestController {

    private static final String TASKS_PATH = "tasks";

    @NonNull
    private final TaskMapper taskMapper;
    @NonNull
    private final TaskService taskService;
    @NonNull
    private final AccountService accountService;
    @NonNull
    private final WorkspacePathResolver workspacePathResolver;
    @NonNull
    private final LogEntryMapper logEntryMapper;
    @NonNull
    private final LogService logService;
    @NonNull
    private final ResourceUri resourceUri;
    @NonNull
    private final ExceptionResponseBuilder exceptionResponseBuilder;

    @RequestMapping(value = "tasks/{taskId}", method = RequestMethod.GET)
    public ResponseEntity getTask(@PathVariable("taskId") Long taskId) {
        Task task = taskService.getTask(taskId);
        TaskTransferObject taskTransferObject = taskMapper.toTaskTransferObject(task);
        return ResponseEntity.ok(taskTransferObject);
    }

    @RequestMapping(value = "workspace/projects/{projectId}/commands/{commandId}", method = RequestMethod.POST)
    public ResponseEntity<Void> runCommandInWorkspace(@PathVariable("projectId") Long projectId, @PathVariable("commandId") Long commandId) {
        Account account = accountService.getActiveAccount();
        Task task = taskService.runCommand(projectId, workspacePathResolver.getPath(), commandId, account.getId());
        URI taskUri = resourceUri.from(TASKS_PATH, task.getId());
        return ResponseEntity.accepted().location(taskUri).build();
    }

    @RequestMapping(value = "workspace/projects/{projectId}/commands/last/logs", method = RequestMethod.GET)
    public ResponseEntity getCommandsOutput(@PathVariable("projectId") Long projectId) {
        List<LogEntry> logEntries = logService.getLogEntries(workspacePathResolver.getPath().resolve(projectId.toString()));
        List<LogEntryTransferObject> logEntriesTransferObjects = logEntryMapper.toLogEntryTransferObjects(logEntries);
        return ResponseEntity.ok(logEntriesTransferObjects);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity onTaskNotFoundException(TaskNotFoundException exception, Locale locale) {
        return exceptionResponseBuilder.buildResponse(exception, NOT_FOUND, locale);
    }

    @ExceptionHandler(TaskNotAcceptedException.class)
    public ResponseEntity onTaskNotAcceptedException(TaskNotAcceptedException exception, Locale locale) {
        return exceptionResponseBuilder.buildResponse(exception, NOT_ACCEPTABLE, locale);
    }

}
