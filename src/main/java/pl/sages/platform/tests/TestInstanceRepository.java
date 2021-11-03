package pl.sages.platform.tests;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestInstanceRepository extends JpaRepository<TestInstance, Long> {

    Optional<TestInstance> getById(Long id);

}
