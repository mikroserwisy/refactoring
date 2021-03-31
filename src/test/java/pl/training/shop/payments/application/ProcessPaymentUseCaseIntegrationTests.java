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
import pl.training.shop.commons.money.FastMoneyConverter;
import pl.training.shop.commons.money.LocalMoney;
import pl.training.shop.payments.adapters.output.providers.SystemTimeProvider;
import pl.training.shop.payments.ports.output.providers.TimeProvider;
import pl.training.shop.payments.PaymentsFixtures;
import pl.training.shop.payments.adapters.output.persistence.JpaPaymentRepository;
import pl.training.shop.payments.domain.Payment;
import pl.training.shop.payments.domain.PaymentStatus;
import pl.training.shop.payments.ports.input.GetPaymentUseCase;
import pl.training.shop.payments.ports.input.ProcessPaymentUseCase;
import pl.training.shop.payments.ports.output.persistence.FindPayment;
import pl.training.shop.payments.ports.output.persistence.SavePayment;

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
class ProcessPaymentUseCaseIntegrationTests {

    @Deployment
    public static JavaArchive createDeployment() {
        var jarArchive = ShrinkWrap.create(JavaArchive.class)
                .addClasses(PaymentsFixtures.class)
                .addClasses(Payment.class, PaymentStatus.class, LocalMoney.class, FastMoneyConverter.class)
                .addClasses(SavePayment.class, FindPayment.class, JpaPaymentRepository.class)
                .addClasses(PaymentIdGenerator.class, UUIDPaymentIdGenerator.class)
                .addClasses(TimeProvider.class, SystemTimeProvider.class)
                .addClasses(GetPaymentUseCase.class, ProcessPaymentService.class, PaymentRequest.class, InvalidPaymentRequest.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("META-INF/beans.xml");
        var dependencies = Maven.resolver().resolve("org.javamoney:moneta:pom:1.4.2")
                .withTransitivity().as(JavaArchive.class);
        return Arrays.stream(dependencies)
                .reduce(jarArchive, Archive::merge);
    }

    @Inject
    private ProcessPaymentUseCase processPaymentUseCase;
    @PersistenceContext(unitName = "shop")
    private EntityManager entityManager;

    @Test
    void given_valid_payment_request_when_process_then_created_payment_is_persisted() {
        var payment = processPaymentUseCase.process(validPaymentRequest);
        var actual = entityManager.find(Payment.class, payment.getId());
        assertNotNull(actual);
        assertEquals(payment.getValue(), actual.getValue());
        assertEquals(payment.getStatus(), actual.getStatus());
    }

}
