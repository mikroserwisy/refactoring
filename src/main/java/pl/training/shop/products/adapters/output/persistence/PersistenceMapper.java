package pl.training.shop.products.adapters.output.persistence;

import org.mapstruct.Mapper;
import pl.training.shop.products.domain.Product;

@Mapper(componentModel = "cdi")
public interface PersistenceMapper {

    ProductEntity toEntity(Product product);

    Product toDomain(ProductEntity productEntity);

}
