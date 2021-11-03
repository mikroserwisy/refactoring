package pl.sages.platform.tests;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TestResultMapper {

    CategoryResultTransferObject toCategoryResultTransferObject(CategoryResult categoryResult);

    @IterableMapping(elementTargetType = CategoryResultTransferObject.class)
    Set<CategoryResultTransferObject> toCategoryResultTransferObjectObjects(Set<CategoryResult> categoryResults);

    TestResultTransferObject toTestResultTransferObject(TestResult testResult);

}
