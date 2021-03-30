package pl.training.shop.payments;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.training.shop.commons.FastMoneyConverter;
import pl.training.shop.commons.LocalMoney;
import pl.training.shop.commons.SystemTimeProvider;
import pl.training.shop.commons.TimeProvider;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static pl.training.Tags.SLOW;
import static pl.training.shop.payments.PaymentsFixtures.validPaymentRequest;

@Tag(SLOW)
@ExtendWith(ArquillianExtension.class)
class PaymentServiceIntegrationTests {

    @Deployment
    public static JavaArchive createDeployment() {
        var jarArchive = ShrinkWrap.create(JavaArchive.class)
                .addClasses(PaymentsFixtures.class)
                .addClasses(Payment.class, PaymentStatus.class, LocalMoney.class, FastMoneyConverter.class)
                .addClasses(PaymentRepository.class, JpaPaymentRepository.class)
                .addClasses(PaymentIdGenerator.class, UUIDPaymentIdGenerator.class)
                .addClasses(TimeProvider.class, SystemTimeProvider.class)
                .addClasses(Payments.class, PaymentService.class, PaymentRequest.class, InvalidPaymentRequest.class)
                .addAsResource("META-INF/persistence.xml");
        var dependencies = Maven.resolver().resolve("org.javamoney:moneta:pom:1.4.2")
                .withTransitivity().as(JavaArchive.class);
        return Arrays.stream(dependencies)
                .reduce(jarArchive, Archive::merge);
    }

    @Inject
    private Payments payments;
    @PersistenceContext(unitName = "shop")
    private EntityManager entityManager;

    @Test
    void given_valid_payment_request_when_process_then_created_payment_is_persisted() {
        var payment = payments.process(validPaymentRequest);
        var actual = entityManager.find(Payment.class, payment.getId());
        assertNotNull(actual);
        assertEquals(payment.getValue(), actual.getValue());
        assertEquals(payment.getStatus(), actual.getStatus());
    }

}
