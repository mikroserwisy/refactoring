package pl.sages.platform.tests;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionInstanceRepository extends JpaRepository<QuestionInstance, Long> {

    Optional<QuestionInstance> getById(Long id);

}
