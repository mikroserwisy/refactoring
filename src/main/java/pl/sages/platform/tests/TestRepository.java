package pl.sages.platform.tests;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestRepository extends JpaRepository<Test, Long> {

    Optional<Test> getById(Long id);

    Optional<Test> getByRepositoryUrl(String repositoryUrl);

}
