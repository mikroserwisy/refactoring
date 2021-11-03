package pl.sages.platform.projects.commands.logs;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogEntryMapper {

    @IterableMapping(elementTargetType = LogEntryTransferObject.class)
    List<LogEntryTransferObject> toLogEntryTransferObjects(List<LogEntry> logEntry);

}
