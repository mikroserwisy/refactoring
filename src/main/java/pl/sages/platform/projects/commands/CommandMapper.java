package pl.sages.platform.projects.commands;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommandMapper {

    @IterableMapping(elementTargetType = CommandTransferObject.class)
    Set<CommandTransferObject> toCommandTransferObjects(Set<Command> commands);

}
