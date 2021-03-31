package pl.training.shop.payments.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.training.shop.payments.domain.Payment;
import pl.training.shop.payments.ports.output.persistence.SavePayment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.training.shop.payments.commons.PaymentsFixtures.*;

@ExtendWith(MockitoExtension.class)
class ProcessPaymentServiceTests {

    @Mock
    private SavePayment paymentRepository;
    private ProcessPaymentService sut;

    @BeforeEach
    void beforeEach() {
        sut = new ProcessPaymentService(() -> PAYMENT_ID, paymentRepository, () -> TIMESTAMP, payment -> {});
    }

    @Test
    void given_a_payment_request_when_process_then_creates_a_valid_payment() {
        when(paymentRepository.save(any(Payment.class))).then(returnsFirstArg());
        assertEquals(expectedPayment, sut.process(validPaymentRequest));
    }

}
