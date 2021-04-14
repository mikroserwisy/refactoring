package pl.training.shop.payments.application;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.training.shop.commons.ArquillianUtils;
import pl.training.shop.payments.commons.PaymentsAssertions;
import pl.training.shop.payments.commons.TimeProviderStub;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import static pl.training.shop.commons.ArquillianUtils.doInTransaction;
import static pl.training.shop.commons.ArquillianUtils.merge;
import static pl.training.shop.payments.commons.PaymentsAssertions.assertPaymentEquals;
import static pl.training.shop.payments.commons.PaymentsFixtures.EXPECTED_PAYMENT;
import static pl.training.shop.payments.commons.PaymentsFixtures.VALID_PAYMENT_REQUEST;

@ExtendWith(ArquillianExtension.class)
public class ProcessPaymentIntegrationTests {

    @Deployment
    public static JavaArchive createDeployment() {
        var javaArchive = ShrinkWrap.create(JavaArchive.class)
                .addClasses(PaymentsFixtures.class, PaymentsAssertions.class, ArquillianUtils.class)
                .addClasses(Payment.class, PaymentStatus.class, LocalMoney.class, FastMoneyConverter.class, PaymentRequest.class, InvalidPaymentRequest.class)
                .addClasses(PaymentRepository.class, JpaPaymentRepository.class)
                .addClasses(PaymentIdGenerator.class, UUIDPaymentIdGenerator.class)
                .addClasses(TimeProvider.class, TimeProviderStub.class)
                .addClasses(Payments.class, PaymentService.class)
                .addAsResource("META-INF/persistence.xml");
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
        doInTransaction(userTransaction, () -> assertPaymentEquals(EXPECTED_PAYMENT, entityManager.find(Payment.class, payment.getId())));
    }

}
