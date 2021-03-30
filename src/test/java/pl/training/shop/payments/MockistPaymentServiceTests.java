package pl.training.shop.payments;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pl.training.shop.payments.PaymentsFixtures.*;

@ExtendWith(MockitoExtension.class)
class MockistPaymentServiceTests {

    @Mock
    private PaymentRepository paymentRepository;
    private PaymentService sut;

    @BeforeEach
    void beforeEach() {
        sut = new PaymentService(() -> PAYMENT_ID, paymentRepository, () -> TIMESTAMP);
    }

    @DisplayName("given a payment request when process")
    @Nested
    class ValidPaymentRequest {

        @BeforeEach
        void beforeEach() {
            when(paymentRepository.save(any(Payment.class))).then(returnsFirstArg());
        }

        @Test
        void then_creates_a_valid_payment() {
            assertEquals(expectedPayment, sut.process(validPaymentRequest));
        }

        @Disabled("This is unnecessary coupling, its better to do an integration test")
        @Test
        void then_created_payment_is_persisted() {
            var payment = sut.process(validPaymentRequest);
            verify(paymentRepository).save(payment);
        }

    }

}
