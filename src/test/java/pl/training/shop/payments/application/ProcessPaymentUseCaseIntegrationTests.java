package pl.training.shop.payments.application;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.training.shop.payments.adapters.output.persistence.JpaPaymentRepository;
import pl.training.shop.payments.commons.DummyPaymentsEventEmitter;
import pl.training.shop.payments.commons.FakeTimeProvider;
import pl.training.shop.payments.ports.input.LogPayment;
import pl.training.shop.payments.ports.output.events.PaymentsEventEmitter;
import pl.training.shop.payments.ports.output.providers.TimeProvider;
import pl.training.shop.payments.commons.PaymentsFixtures;
import pl.training.shop.payments.domain.Payment;
import pl.training.shop.payments.ports.input.ProcessPaymentUseCase;
import pl.training.shop.payments.ports.output.persistence.SavePayment;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static pl.training.Tags.SLOW;
import static pl.training.shop.payments.commons.PaymentsFixtures.validPaymentRequest;

@Tag(SLOW)
@ExtendWith(ArquillianExtension.class)
class ProcessPaymentUseCaseIntegrationTests {

    @Deployment
    public static JavaArchive createDeployment() {
        var jarArchive = ShrinkWrap.create(JavaArchive.class)
                .addClasses(PaymentsFixtures.class)
                .addPackage("pl.training.shop.commons")
                .addPackage("pl.training.shop.domain")
                .addPackage("pl.training.shop.application")
                .addClasses(LogPayment.class, PaymentsEventEmitter.class, SavePayment.class, TimeProvider.class)
                .addClasses(JpaPaymentRepository.class, DummyPaymentsEventEmitter.class, FakeTimeProvider.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("META-INF/beans.xml");
        var dependencies = Maven.resolver().resolve("org.javamoney:moneta:pom:1.4.2")
                .withTransitivity().as(JavaArchive.class);
        return Arrays.stream(dependencies)
                .reduce(jarArchive, Archive::merge);
    }

    @Inject
    private ProcessPaymentUseCase processPaymentUseCase;
    @Inject
    private TimeProvider timeProvider;
    @PersistenceContext(unitName = "shop")
    private EntityManager entityManager;

    @Test
    void given_valid_payment_request_when_process_then_created_payment_is_persisted() {
        var payment = processPaymentUseCase.process(validPaymentRequest);
        var actual = entityManager.find(Payment.class, payment.getId());
        assertNotNull(actual);
        assertEquals(payment.getValue(), actual.getValue());
        assertEquals(payment.getStatus(), actual.getStatus());
        assertEquals(timeProvider.getTimestamp(), actual.getTimestamp());
    }

}
