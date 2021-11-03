package pl.sages.platform.projects.commands.logs;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.sages.platform.common.configuration.TransactionalService;

import java.nio.file.Path;
import java.util.List;

@TransactionalService
@RequiredArgsConstructor
public class LogService {

    @NonNull
    private final LogEntryRepository logEntryRepository;

    public void addLogEntry(LogEntry logEntry) {
        logEntryRepository.saveAndFlush(logEntry);
    }

    public List<LogEntry> getLogEntries(Path projectPath) {
        return logEntryRepository.getByProjectPathOrderByTimestampAsc(projectPath.toString());
    }

    public void deleteLogEntries(Path projectPath) {
        logEntryRepository.deleteByProjectPath(projectPath.toString());
    }

}
