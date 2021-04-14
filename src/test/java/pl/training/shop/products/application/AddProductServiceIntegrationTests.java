package pl.training.shop.products.application;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.training.shop.commons.ArquillianUtils;
import pl.training.shop.payments.commons.PaymentsAssertions;
import pl.training.shop.payments.commons.PaymentsFixtures;
import pl.training.shop.products.adapters.output.persistence.ProductEntity;
import pl.training.shop.products.domain.Product;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static pl.training.shop.commons.Tags.INTEGRATION;

@Tag(INTEGRATION)
@ExtendWith(ArquillianExtension.class)
public class AddProductServiceIntegrationTests {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(PaymentsFixtures.class, PaymentsAssertions.class, ArquillianUtils.class)
                .addPackage("pl.training.shop.products.domain")
                .addPackage("pl.training.shop.products.application")
                .addPackage("pl.training.shop.products.ports.input")
                .addPackage("pl.training.shop.products.adapters.output.persistence")
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("META-INF/beans.xml");
    }

    @Inject
    private AddProductService sut;
    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void init() {
        entityManager.createQuery("delete from ProductEntity p").executeUpdate();
    }

    @Test
    void given_a_new_product_when_add_then_the_product_is_persisted() {
        var product = sut.add(new Product(1L, false));
        var result = entityManager.find(ProductEntity.class, product.getId());
        assertNotNull(result);
    }

}
