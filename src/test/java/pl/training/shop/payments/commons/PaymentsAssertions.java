package pl.training.shop.payments.commons;

import pl.training.shop.payments.adapters.output.persistence.PaymentEntity;
import pl.training.shop.payments.domain.Payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PaymentsAssertions {

    public static void assertPaymentEquals(Payment expected, PaymentEntity actual) {
        assertNotNull(actual);
        assertEquals(expected.getValue(), actual.getValue());
        assertEquals(expected.getStatus().name(), actual.getStatus());
        assertEquals(expected.getTimestamp(), actual.getTimestamp());
        assertEquals(expected.getProperties(), actual.getAdditionalProperties());
    }

}
