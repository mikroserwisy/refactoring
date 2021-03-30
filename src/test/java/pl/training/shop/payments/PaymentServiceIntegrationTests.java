package pl.training.shop.payments;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static pl.training.Tags.SLOW;

@Tag(SLOW)
@ExtendWith(ArquillianExtension.class)
class PaymentServiceIntegrationTests {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(PaymentIdGenerator.class, UUIDPaymentIdGenerator.class);
    }

    @Test
    void given_valid_payment_request_when_process_then_created_payment_is_persisted() {
        
    }

}
