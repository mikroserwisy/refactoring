package pl.training.shop.payments.application;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.training.shop.commons.ArquillianUtils;
import pl.training.shop.payments.adapters.output.events.KafkaPaymentsEventEmitter;
import pl.training.shop.payments.adapters.output.persistence.JpaPaymentRepository;
import pl.training.shop.payments.adapters.output.persistence.PaymentEntity;
import pl.training.shop.payments.adapters.output.persistence.PersistenceMapper;
import pl.training.shop.payments.commons.PaymentsAssertions;
import pl.training.shop.payments.commons.PaymentsFixtures;
import pl.training.shop.payments.commons.TimeProviderStub;
import pl.training.shop.payments.domain.Payment;
import pl.training.shop.payments.ports.input.PaymentProcess;
import pl.training.shop.payments.ports.input.ProcessPaymentUseCase;
import pl.training.shop.payments.ports.output.events.PaymentsEventEmitter;
import pl.training.shop.payments.ports.output.persistence.PaymentUpdates;
import pl.training.shop.payments.ports.output.providers.TimeProvider;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import static pl.training.shop.commons.ArquillianUtils.doInTransaction;
import static pl.training.shop.commons.ArquillianUtils.merge;
import static pl.training.shop.commons.Tags.INTEGRATION;
import static pl.training.shop.payments.commons.PaymentsAssertions.assertPaymentEquals;
import static pl.training.shop.payments.commons.PaymentsFixtures.EXPECTED_PAYMENT;
import static pl.training.shop.payments.commons.PaymentsFixtures.VALID_PAYMENT_REQUEST;

@Tag(INTEGRATION)
@ExtendWith(ArquillianExtension.class)
public class ProcessPaymentIntegrationTests {

    @Deployment
    public static JavaArchive createDeployment() {
        var javaArchive = ShrinkWrap.create(JavaArchive.class)
                .addClasses(PaymentsFixtures.class, PaymentsAssertions.class, ArquillianUtils.class)
                .addPackages(true, "pl.training.shop.commons")
                .addPackages(true, "pl.training.shop.payments.domain")
                .addPackages(true,"pl.training.shop.payments.application")
                .addPackages(true,"pl.training.shop.payments.ports")
                .addPackage("pl.training.shop.payments.adapters.output.persistence")
                .addPackage("pl.training.shop.payments.adapters.output.events")
                .addClasses(PaymentProcess.class, TimeProviderStub.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("META-INF/beans.xml");
        return merge(javaArchive, "org.javamoney:moneta:pom:1.4.2");
    }

    @Inject
    private ProcessPaymentService sut;
    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private UserTransaction userTransaction;

    @Test
    void given_a_payment_request_when_process_the_created_payment_is_persisted() {
        var payment = sut.process(VALID_PAYMENT_REQUEST);
        doInTransaction(userTransaction, () -> assertPaymentEquals(EXPECTED_PAYMENT, entityManager.find(PaymentEntity.class, payment.getId())));
    }

}
