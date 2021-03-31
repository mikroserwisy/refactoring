package pl.training.shop.products.application;

import lombok.SneakyThrows;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static pl.training.Tags.SLOW;

@Tag(SLOW)
@ExtendWith(ArquillianExtension.class)
class AddProductServiceIntegrationTests {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage("pl.training.shop.commons")
                .addPackage("pl.training.shop.products")
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("META-INF/beans.xml");
    }

    @Inject
    private AddProductService sut;
    @Inject
    private EntityManager entityManager;
    @Inject
    private UserTransaction userTransaction;

    @SneakyThrows
    @BeforeEach
    void init() {
        userTransaction.begin();
        entityManager.createQuery("delete from ProductEntity p").executeUpdate();
        userTransaction.commit();
    }

    @Test
    void given_new_product_when_is_added_then_product_is_persisted() {

    }

}
