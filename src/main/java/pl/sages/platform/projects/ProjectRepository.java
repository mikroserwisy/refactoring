package pl.sages.platform.projects;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> getById(Long id);

    Optional<Project> getByRepositoryUrl(String repositoryUrl);

}
