package pl.sages.platform.tests;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TestInstanceMapper {

    AnswerInstanceTransferObject toAnswerInstanceTransferObject(AnswerInstance answerInstance);

    @IterableMapping(elementTargetType = AnswerInstanceTransferObject.class)
    Set<AnswerInstanceTransferObject> toAnswerInstanceTransferObjects(Set<AnswerInstance> answerInstances);

    QuestionInstanceTransferObject toQuestionInstanceTransferObject(QuestionInstance questionInstance);

    @IterableMapping(elementTargetType = QuestionInstanceTransferObject.class)
    Set<QuestionInstanceTransferObject> toQuestionInstanceTransferObjects(Set<QuestionInstance> questionInstances);

    TestInstanceTransferObject toTestInstanceTransferObject(TestInstance testInstance);

    AnswerInstance toAnswerInstance(AnswerInstanceTransferObject answerInstanceTransferObject);

    @IterableMapping(elementTargetType = AnswerInstance.class)
    Set<AnswerInstance> toAnswerInstanceObjects(Set<AnswerInstanceTransferObject> answerInstanceTransferObjects);

    QuestionInstance toQuestionInstance(QuestionInstanceTransferObject questionInstanceTransferObject);

    @IterableMapping(elementTargetType = QuestionInstance.class)
    Set<QuestionInstance> toQuestionInstance(Set<QuestionInstanceTransferObject> questionInstanceTransferObjects);

}
