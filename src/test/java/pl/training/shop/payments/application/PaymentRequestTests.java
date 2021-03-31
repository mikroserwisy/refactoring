package pl.training.shop.payments.application;

import org.junit.jupiter.api.Test;
import pl.training.shop.commons.money.LocalMoney;

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentRequestTests {

    @Test
    void given_a_money_value_lower_than_one_when_new_payment_request_is_created_then_throws_an_exception() {
        assertThrows(InvalidPaymentRequest.class, () -> new PaymentRequest(LocalMoney.zero(), emptyMap()));
    }

}
