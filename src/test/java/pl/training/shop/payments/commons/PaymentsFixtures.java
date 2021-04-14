package pl.training.shop.payments.commons;

import org.javamoney.moneta.FastMoney;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import pl.training.shop.commons.ArquillianUtils;
import pl.training.shop.commons.money.LocalMoney;
import pl.training.shop.payments.application.PaymentRequest;
import pl.training.shop.payments.domain.Payment;

import java.time.Instant;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static pl.training.shop.payments.domain.PaymentStatus.STARTED;

class PaymentsFixtures {

    static final String PAYMENT_ID = "aed6670f-21d8-44ed-9e82-4543e0839518";
    static final Instant TIMESTAMP = Instant.now();
    static final FastMoney MONEY = LocalMoney.of(1_000);
    static final Map<String, String> PAYMENT_PROPERTIES = emptyMap();
    static final Payment EXPECTED_PAYMENT = Payment.builder()
            .id(PAYMENT_ID)
            .value(MONEY)
            .status(STARTED)
            .timestamp(TIMESTAMP)
            .properties(PAYMENT_PROPERTIES)
            .build();
    static final PaymentRequest VALID_PAYMENT_REQUEST = new PaymentRequest(MONEY, PAYMENT_PROPERTIES);

}
