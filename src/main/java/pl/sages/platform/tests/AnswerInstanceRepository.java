package pl.sages.platform.tests;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerInstanceRepository extends JpaRepository<AnswerInstance, Long> {

    Optional<AnswerInstance> getById(Long id);

}
