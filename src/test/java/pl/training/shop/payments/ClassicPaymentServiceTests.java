package pl.training.shop.payments;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.training.shop.payments.PaymentsFixtures.*;

public class ClassicPaymentServiceTests {

    private final PaymentService sut = new PaymentService(() -> PAYMENT_ID, new InMemoryPaymentRepository(), () -> TIMESTAMP);

    @Test
    void given_a_payment_request_when_process_then_creates_a_valid_payment() {
        assertEquals(expectedPayment, sut.process(validPaymentRequest));
    }

}
