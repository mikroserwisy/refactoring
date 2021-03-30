package pl.training.shop.payments;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UUIDPaymentIdGeneratorTests {

    private static final String ID_FORMAT = "\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}";

    private final UUIDPaymentIdGenerator sut = new UUIDPaymentIdGenerator();

    @Test
    void when_get_next_then_returns_valid_payment_id() {
        assertTrue(sut.getNext().matches(ID_FORMAT));
    }

}
