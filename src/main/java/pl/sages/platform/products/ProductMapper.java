package pl.sages.platform.products;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.sages.platform.common.pagination.ResultPage;
import pl.sages.platform.common.pagination.ResultPageTransferObject;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    Product toProduct(ProductTransferObject productTransferObject);

    @IterableMapping(elementTargetType = ProductElement.class)
    List<ProductElement> toProductElementObjects(List<ProductElementTransferObject> productElementTransferObject);

    @IterableMapping(elementTargetType = ProductTransferObject.class)
    List<ProductTransferObject> toProductTransferObjects(Set<Product> products);

    ResultPageTransferObject<ProductTransferObject> toProductTransferObjects(ResultPage<Product> productResultPage);

}
