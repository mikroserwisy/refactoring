package pl.sages.platform.projects.commands.logs;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {

    List<LogEntry> getByProjectPathOrderByTimestampAsc(String projectPath);

    void deleteByProjectPath(String projectPath);

}
