package pl.sages.platform.repositories;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RepositoryMapper {

    Repository toRepository(RepositoryTransferObject repositoryTransferObject);

}
