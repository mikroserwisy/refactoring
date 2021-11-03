package pl.sages.platform.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pl.sages.platform.accounts.Account;
import pl.sages.platform.accounts.AccountService;
import pl.sages.platform.common.configuration.TransactionalService;
import pl.sages.platform.common.pagination.ResultPage;
import pl.sages.platform.products.Product;
import pl.sages.platform.products.ProductElementService;
import pl.sages.platform.products.ProductElementType;
import pl.sages.platform.projects.InvalidProjectMetadataException;
import pl.sages.platform.repositories.Repository;
import pl.sages.platform.repositories.RepositoryService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@TransactionalService
@RequiredArgsConstructor
public class TestService {

    private static final String RELATIVE_METADATA_PATH = "metadata/test.json";
    private static final String REPOSITORIES_FOLDER = "repositories";

    @NonNull
    private final RepositoryService repositoryService;
    @NonNull
    private final ProductElementService productElementService;
    @NonNull
    private final TestRepository testRepository;
    @NonNull
    private final TestInstanceRepository testInstanceRepository;
    @NonNull
    private final QuestionInstanceRepository questionInstanceRepository;
    @NonNull
    private final ObjectMapper objectMapper;
    @NonNull
    private final AccountService accountService;
    @NonNull
    private final TestMapper testMapper;
    @Value("${platform.storage-path}")
    @Setter
    private Path storagePath;


    public void importTest(Repository repository) {
        try {
            Path relativeLocalPath = createLocalPath(repository.getRemoteUrl());
            Path absoluteLocalPath = storagePath.resolve(REPOSITORIES_FOLDER).resolve(relativeLocalPath);
            repository.setLocalPath(absoluteLocalPath);
            if (Files.notExists(absoluteLocalPath)) {
                repositoryService.cloneRepository(repository);
            } else {
                repositoryService.pullChanges(repository);
            }
            importTestMetadata(relativeLocalPath, absoluteLocalPath);
        } catch (IOException e) {
            repositoryService.deleteRepository(repository.getLocalPath());
            throw new InvalidProjectMetadataException();
        }
    }

    private Path createLocalPath(String remoteUrl) throws MalformedURLException {
        URL url = new URL(remoteUrl);
        return Path.of(url.getHost(), url.getPath());
    }

    private void importTestMetadata(Path relativeLocalPath, Path absoluteLocalPath) throws IOException {
        Path metadataPath = absoluteLocalPath.resolve(RELATIVE_METADATA_PATH);
        Test test = objectMapper.readValue(metadataPath.toFile(), Test.class);
        Optional<Test> oldTest = testRepository.getByRepositoryUrl(relativeLocalPath.toString());
        test.setRepositoryUrl(relativeLocalPath.toString());
        test.setImportTimestamp(System.currentTimeMillis());
        int index = 0;
        for (Question question : test.getQuestions()) {
            question.setIndex(++index);
        }
        testRepository.saveAndFlush(test);
        if (oldTest.isPresent()) {
            productElementService.updateElementId(oldTest.get().getId(), test.getId());
            testRepository.delete(oldTest.get());
        }
    }

    public void deleteTest(Long testId) {
        productElementService.isElementSaveToDelete(testId);
        Test test = getTest(testId);
        testRepository.delete(test);
        Path repositoryPath = storagePath.resolve(REPOSITORIES_FOLDER).resolve(test.getRepositoryUrl());
        repositoryService.deleteRepository(repositoryPath);
    }

    private Test getTest(Long testId) {
        return testRepository.getById(testId)
                .orElseThrow(TestNotFoundException::new);
    }

    public TestInstance createTestInstance(Long testId) {
        Account account = accountService.getActiveAccount();
        Long activeElementId = account.getActiveElementId();
        if (activeElementId != null && account.getActiveElementType().equals(ProductElementType.TEST)) {
            return getTestInstance(activeElementId);
        }
        Test test = testRepository.getById(testId)
                .orElseThrow(TestNotFoundException::new);
        TestInstance testInstance = testMapper.toTestInstance(test);
        testInstance.setStartTime(new Date());
        testInstanceRepository.saveAndFlush(testInstance);
        testInstanceRepository.saveAndFlush(testInstance);
        account.setActiveElementId(testInstance.getId());
        account.setActiveElementType(ProductElementType.TEST);
        accountService.update(account);
        return testInstance;
    }

    public TestInstance answerQuestion(Long testInstanceId, Long questionId, Set<AnswerInstance> answerInstances) {
        TestInstance testInstance = testInstanceRepository.getById(testInstanceId)
                .orElseThrow(TestNotFoundException::new);
        if (testInstance.shouldBeFinished()) {
            testInstance.finishTest();
            return testInstanceRepository.saveAndFlush(testInstance);
        } else {
            QuestionInstance questionInstance = questionInstanceRepository.getById(questionId)
                    .orElseThrow(QuestionNotFoundException::new);
            testInstance.setLastUpdateTime(new Date());
            testInstance.setCurrentQuestionIndex(questionInstance.getIndex());
            Set<AnswerInstance> oldAnswerInstances = questionInstance.getAnswers();
            answerInstances.forEach(answerInstance -> updateAnswer(oldAnswerInstances, answerInstance));
            boolean isQuestionCorrect = questionInstance.getAnswers().stream()
                    .filter(answerInstance -> answerInstance.getValue() != null)
                    .map(answerInstance -> answerInstance.getValue().equals(answerInstance.getCorrectValue()))
                    .reduce((firstValue, secondValue) -> firstValue && secondValue)
                    .orElse(false);
            questionInstance.setCorrect(isQuestionCorrect);
            questionInstanceRepository.saveAndFlush(questionInstance);
            return testInstance;
        }
    }

    private void updateAnswer(Set<AnswerInstance> oldAnswerInstances, AnswerInstance answerInstance) {
        AnswerInstance oldAnswerInstance = oldAnswerInstances.stream()
                .filter(instance -> instance.getId().equals(answerInstance.getId()))
                .findFirst()
                .orElseThrow(AnswerNotFoundException::new);
        oldAnswerInstance.setValue(answerInstance.getValue());
    }

    public TestInstance getTestInstance(Long testInstanceId) {
        TestInstance testInstance = testInstanceRepository.getById(testInstanceId)
                .orElseThrow(TestNotFoundException::new);
        if (testInstance.shouldBeFinished()) {
            finishTest(testInstanceId);
            return testInstanceRepository.saveAndFlush(testInstance);
        } else {
            return testInstance;
        }
    }

    public void finishTest(Long testInstanceId) {
        TestInstance testInstance = testInstanceRepository.getById(testInstanceId)
                .orElseThrow(TestNotFoundException::new);
        testInstance.finishTest();
        testInstanceRepository.saveAndFlush(testInstance);
        Account account = accountService.getActiveAccount();
        account.setActiveElementId(null);
        account.setActiveElementType(null);
        accountService.update(account);
    }

    public TestResult getTestResult(Long testInstanceId) {
        TestInstance testInstance = testInstanceRepository.getById(testInstanceId)
                .orElseThrow(TestNotFoundException::new);
        if (testInstance.shouldBeFinished()) {
            finishTest(testInstanceId);
        } else if (!testInstance.isFinished()) {
            throw new TestIsNotFinishedException();
        }
        TestResult testResult = new TestResult();
        testResult.setNumberOfQuestions(testInstance.getQuestions().size());
        long numberOfCorrectQuestions = testInstance.getQuestions().stream()
                .map(QuestionInstance::isCorrect)
                .filter(value -> value)
                .count();
        testResult.setNumberOfCorrectQuestions(numberOfCorrectQuestions);
        testResult.setRequiredScorePercentage(testInstance.getRequiredScorePercentage());
        boolean passed = (numberOfCorrectQuestions * 100 / testResult.getNumberOfQuestions()) >= testInstance.getRequiredScorePercentage();
        testResult.setPassed(passed);
        testResult.setTimeInSeconds(testInstance.getTotalTime());
        List<CategoryResult> categoryResults = testInstance.getQuestions().stream()
                .collect(Collectors.groupingBy(QuestionInstance::getCategory))
                .entrySet().stream()
                .map(this::getCategoryResult)
                .collect(Collectors.toList());
        testResult.setCategoryResults(categoryResults);
        return testResult;
    }

    private CategoryResult getCategoryResult(Map.Entry<String, List<QuestionInstance>> entry) {
        List<QuestionInstance> questionInstances = entry.getValue();
        CategoryResult categoryResult = new CategoryResult();
        categoryResult.setName(entry.getKey());
        categoryResult.setNumberOfQuestions(questionInstances.size());
        categoryResult.setNumberOfCorrectQuestions(questionInstances.stream()
                .filter(QuestionInstance::isCorrect)
                .count());
        return categoryResult;
    }

    public ResultPage<Test> getTests(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Test> testsPage = testRepository.findAll(pageRequest);
        return new ResultPage<>(testsPage.getContent(), pageNumber, testsPage.getTotalPages());
    }

}
