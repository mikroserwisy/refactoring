package pl.sages.platform.projects.tasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select count(t) from Task t where t.ownerId = :ownerId and t.status in ('PENDING', 'RUNNING')")
    int getNotFinishedTasksCount(@Param("ownerId") Long ownerId);

    @Modifying
    @Transactional
    @Query("delete from Task t where t.startedAt < :date")
    void deleteOldTasks(@Param("date") Date date);

}
