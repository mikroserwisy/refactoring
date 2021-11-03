package pl.sages.platform.tests;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sages.platform.common.IdentifierTransferObject;
import pl.sages.platform.common.ResourceUri;
import pl.sages.platform.common.exception.ExceptionResponseBuilder;
import pl.sages.platform.common.pagination.ResultPage;
import pl.sages.platform.common.pagination.ResultPageTransferObject;
import pl.sages.platform.products.ElementInUseException;
import pl.sages.platform.projects.Project;
import pl.sages.platform.projects.ProjectSummaryTransferObject;
import pl.sages.platform.repositories.Repository;
import pl.sages.platform.repositories.RepositoryMapper;
import pl.sages.platform.repositories.RepositoryTransferObject;

import java.net.URI;
import java.util.Locale;
import java.util.Set;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequestMapping("${platform.api-prefix}")
@RestController
@RequiredArgsConstructor
public class TestRestController {

    private static final String TEST_INSTANCES_PATH = "tests/instances";

    @NonNull
    private final TestService testService;
    @NonNull
    private final RepositoryMapper repositoryMapper;
    @NonNull
    private final TestInstanceMapper testInstanceMapper;
    @NonNull
    private final TestResultMapper testResultMapper;
    @NonNull
    private final TestMapper testMapper;
    @NonNull
    private final ResourceUri resourceUri;
    @NonNull
    private final ExceptionResponseBuilder exceptionResponseBuilder;

    @RequestMapping(value = "repositories/tests", method = RequestMethod.POST)
    public ResponseEntity<Void> importTest(@RequestBody RepositoryTransferObject repositoryTransferObject) {
        Repository repository = repositoryMapper.toRepository(repositoryTransferObject);
        testService.importTest(repository);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "tests", method = RequestMethod.GET)
    public ResponseEntity<ResultPageTransferObject<TestSummaryTransferObject>> getTestsSummaries(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "50") int pageSize) {
        ResultPage<Test> projectsPage = testService.getTests(pageNumber, pageSize);
        ResultPageTransferObject<TestSummaryTransferObject> resultPageTransferObject = testMapper.toTestSummaryTransferObjects(projectsPage);
        return ResponseEntity.ok(resultPageTransferObject);
    }

    @RequestMapping(value = "tests/{testId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteTest(@PathVariable("testId") Long testId) {
        testService.deleteTest(testId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "tests/instances", method = RequestMethod.POST)
    public ResponseEntity<TestInstanceTransferObject> startTest(@RequestBody IdentifierTransferObject identifierTransferObject) {
        TestInstance testInstance = testService.createTestInstance(identifierTransferObject.getId());
        TestInstanceTransferObject testInstanceTransferObject = testInstanceMapper.toTestInstanceTransferObject(testInstance);
        URI testInstanceUri = resourceUri.from(TEST_INSTANCES_PATH, testInstance.getId());
        return ResponseEntity.created(testInstanceUri).body(testInstanceTransferObject);
    }

    @RequestMapping(value = "tests/instances/{testId}/questions/{questionId}", method = RequestMethod.PUT)
    public ResponseEntity<TestInstanceTransferObject> answerQuestion(@RequestBody Set<AnswerInstanceTransferObject> answerInstanceTransferObjects, @PathVariable("testId") Long testId, @PathVariable("questionId") Long questionId) {
        Set<AnswerInstance> answerInstances = testInstanceMapper.toAnswerInstanceObjects(answerInstanceTransferObjects);
        TestInstance testInstance = testService.answerQuestion(testId, questionId, answerInstances);
        TestInstanceTransferObject testInstanceTransferObject = testInstanceMapper.toTestInstanceTransferObject(testInstance);
        return ResponseEntity.ok(testInstanceTransferObject);
    }

    @RequestMapping(value = "tests/instances/{testId}", method = RequestMethod.GET)
    public ResponseEntity<TestInstanceTransferObject> getTestInstance(@PathVariable("testId") Long testId) {
        TestInstance testInstance = testService.getTestInstance(testId);
        TestInstanceTransferObject testInstanceTransferObject = testInstanceMapper.toTestInstanceTransferObject(testInstance);
        return ResponseEntity.ok(testInstanceTransferObject);
    }

    @RequestMapping(value = "tests/instances/{testId}", method = RequestMethod.PATCH)
    public ResponseEntity<Void> finishTest(@PathVariable("testId") Long testId) {
        testService.finishTest(testId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "tests/instances/{testId}/results", method = RequestMethod.GET)
    public ResponseEntity<TestResultTransferObject> getTestInstanceResults(@PathVariable("testId") Long testId) {
        TestResult testResult = testService.getTestResult(testId);
        TestResultTransferObject testResultTransferObject = testResultMapper.toTestResultTransferObject(testResult);
        return ResponseEntity.ok(testResultTransferObject);
    }

    @ExceptionHandler(InvalidTestMetadataException.class)
    public ResponseEntity onInvalidTestMetadataException(InvalidTestMetadataException exception, Locale locale) {
        return exceptionResponseBuilder.buildResponse(exception, BAD_REQUEST, locale);
    }

    @ExceptionHandler(TestIsNotFinishedException.class)
    public ResponseEntity onTestIsNotFinishedException(TestIsNotFinishedException exception, Locale locale) {
        return exceptionResponseBuilder.buildResponse(exception, BAD_REQUEST, locale);
    }

    @ExceptionHandler(TestNotFoundException.class)
    public ResponseEntity onTestNotFoundException(TestNotFoundException exception, Locale locale) {
        return exceptionResponseBuilder.buildResponse(exception, NOT_FOUND, locale);
    }

    @ExceptionHandler(ElementInUseException.class)
    public ResponseEntity onProductElementInUseException(ElementInUseException exception, Locale locale) {
        return exceptionResponseBuilder.buildResponse(exception, BAD_REQUEST, locale);
    }

}
