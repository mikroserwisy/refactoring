package pl.sages.platform.tests;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.sages.platform.common.pagination.ResultPage;
import pl.sages.platform.common.pagination.ResultPageTransferObject;
import pl.sages.platform.projects.Project;
import pl.sages.platform.projects.ProjectSummaryTransferObject;
import pl.sages.platform.projects.ProjectTransferObject;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TestMapper {

    @Mapping(target="id", ignore = true)
    AnswerInstance toAnswerInstance(Answer answer);

    @IterableMapping(elementTargetType = AnswerInstance.class)
    Set<AnswerInstance> toAnswerInstanceObjects(Set<Answer> answers);

    @Mapping(target="id", ignore = true)
    QuestionInstance toQuestionInstance(Question question);

    @IterableMapping(elementTargetType = QuestionInstance.class)
    Set<QuestionInstance> toQuestionInstanceObjects(Set<Question> questions);

    @Mapping(target="id", ignore = true)
    TestInstance toTestInstance(Test test);

    @IterableMapping(elementTargetType = TestSummaryTransferObject.class)
    List<TestSummaryTransferObject> toTestSummaryTransferObjects(List<Test> projects);

    ResultPageTransferObject<TestSummaryTransferObject> toTestSummaryTransferObjects(ResultPage<Test> testResultPage);

}
