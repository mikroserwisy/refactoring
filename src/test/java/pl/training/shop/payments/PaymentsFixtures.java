package pl.training.shop.payments;

import org.javamoney.moneta.FastMoney;
import pl.training.shop.commons.money.LocalMoney;

import java.time.Instant;

import static java.util.Collections.emptyMap;

class PaymentsFixtures {

    static final FastMoney MONEY = LocalMoney.of(1_000);
    static final String PAYMENT_ID = "7e098f3d-076c-4ab8-9c74-7d1935efb501";
    static final Instant TIMESTAMP = Instant.now();
    static final PaymentRequest validPaymentRequest = new PaymentRequest(MONEY, emptyMap());
    static final Payment expectedPayment = Payment.builder()
            .id(PAYMENT_ID)
            .value(MONEY)
            .properties(emptyMap())
            .timestamp(TIMESTAMP)
            .status(PaymentStatus.STARTED)
            .build();

}
