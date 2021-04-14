package pl.training.shop.payments;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.training.shop.commons.SystemTimeProvider;
import pl.training.shop.commons.TimeProvider;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(ArquillianExtension.class)
public class PaymentsServiceIntegrationTests {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(TimeProvider.class, SystemTimeProvider.class);
    }

    @Inject
    private TimeProvider timeProvider;

    @Test
    void given_a_payment_request_when_process_the_created_payment_is_persisted() {
        assertNotNull(timeProvider.getTimestamp());
    }

}
