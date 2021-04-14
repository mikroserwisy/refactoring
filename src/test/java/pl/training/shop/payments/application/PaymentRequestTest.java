package pl.training.shop.payments.application;

import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.training.shop.commons.LocalMoney.zero;

class PaymentRequestTest {

    @Test
    void given_a_money_value_lower_than_one_when_new_payment_request_is_created_then_throws_an_exception() {
        assertThrows(InvalidPaymentRequest.class, () -> new PaymentRequest(zero(), emptyMap()));
    }

}
