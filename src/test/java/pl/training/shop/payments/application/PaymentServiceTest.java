package pl.training.shop.payments.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    private PaymentService sut;

    @BeforeEach
    void beforeEach() {
        sut = new PaymentService(() -> PAYMENT_ID, new InMemoryPaymentRepository(), () -> TIMESTAMP);
    }

    @Test
    void given_a_payment_request_when_process_then_returns_a_valid_payment() {
        assertEquals(EXPECTED_PAYMENT, sut.process(VALID_PAYMENT_REQUEST));
    }

}
