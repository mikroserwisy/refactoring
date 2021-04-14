package pl.training.shop.products.adapters.output.persistence;

import lombok.Setter;
import pl.training.shop.products.domain.Product;
import pl.training.shop.products.ports.output.persistence.ProductUpdates;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class JpaProductRepository implements ProductUpdates {

    @Inject
    @Setter
    private PersistenceMapper persistenceMapper;
    @PersistenceContext(unitName = "shop")
    private EntityManager entityManager;

    @Override
    public Product save(Product product) {
        var entity = persistenceMapper.toEntity(product);
        entityManager.persist(entity);
        entityManager.flush();
        entityManager.refresh(entity);
        return persistenceMapper.toDomain(entity);
    }

}
