package pl.sages.platform.tests;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Table(name = "test_instance")
@Entity
@Data
public class TestInstance {

    @GeneratedValue
    @Id
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time")
    private Date startTime;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    private Date endTime;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update_time")
    private Date lastUpdateTime;
    @JoinColumn(name = "test_id")
    @Column(name = "is_finished", nullable = false)
    private boolean isFinished;
    @Column(name = "current_question_index")
    private Integer currentQuestionIndex;
    @JoinColumn(name = "test_instance_id")
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private Set<QuestionInstance> questions;
    @Column(name = "time_limit")
    private long timeLimitSeconds;
    @Column(name = "required_score_percentage")
    private int requiredScorePercentage;

    public boolean shouldBeFinished() {
        if (timeLimitSeconds == 0) {
            return false;
        }
        return System.currentTimeMillis() > startTime.getTime() + timeLimitSeconds / 1000;
    }

    public void finishTest() {
        long currentTime = System.currentTimeMillis();
        long maxTime =  startTime.getTime() + timeLimitSeconds;
        endTime  = new Date(currentTime >= maxTime ? maxTime : currentTime);
        setFinished(true);
    }

    public long getTotalTime() {
        return endTime.getTime() - startTime.getTime();
    }

}
