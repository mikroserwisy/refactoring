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
import pl.training.shop.payments.adapters.output.persistence.PaymentEntity;
import pl.training.shop.payments.commons.DummyPaymentsEventEmitter;
import pl.training.shop.payments.commons.FakeTimeProvider;
import pl.training.shop.payments.commons.PaymentsFixtures;
import pl.training.shop.payments.ports.input.LogPayment;
import pl.training.shop.payments.ports.output.events.PaymentsEventEmitter;
import pl.training.shop.payments.ports.output.persistence.PaymentUpdates;
import pl.training.shop.payments.ports.output.providers.TimeProvider;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static pl.training.Tags.SLOW;
import static pl.training.shop.payments.commons.PaymentsFixtures.validPaymentRequest;

@Tag(SLOW)
@ExtendWith(ArquillianExtension.class)
class ProcessPaymentServiceIntegrationTests {

    @Deployment
    public static JavaArchive createDeployment() {
        var jarArchive = ShrinkWrap.create(JavaArchive.class)
                .addClasses(PaymentsFixtures.class)
                .addPackage("pl.training.shop.commons")
                .addPackage("pl.training.shop.payments.domain")
                .addPackage("pl.training.shop.payments.application")
                .addPackage("pl.training.shop.adapters.output.persistence")
                .addClasses(LogPayment.class, PaymentsEventEmitter.class, PaymentUpdates.class, TimeProvider.class)
                .addClasses(DummyPaymentsEventEmitter.class, FakeTimeProvider.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("META-INF/beans.xml");
        var dependencies = Maven.resolver().resolve("org.javamoney:moneta:pom:1.4.2")
                .withTransitivity().as(JavaArchive.class);
        return Arrays.stream(dependencies)
                .reduce(jarArchive, Archive::merge);
    }

    @Inject
    private ProcessPaymentService sut;
    @Inject
    private TimeProvider timeProvider;
    @Inject
    private EntityManager entityManager;

    @Test
    void given_valid_payment_request_when_process_then_created_payment_is_persisted() {
        var payment = sut.process(validPaymentRequest);
        var actual = entityManager.find(PaymentEntity.class, payment.getId());
        assertNotNull(actual);
        assertEquals(payment.getValue(), actual.getValue());
        assertEquals(payment.getStatus().name(), actual.getStatus());
        assertEquals(timeProvider.getTimestamp(), actual.getTimestamp());
    }

}
