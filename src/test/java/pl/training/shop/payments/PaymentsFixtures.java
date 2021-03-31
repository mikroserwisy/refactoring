package pl.training.shop.payments;

import org.javamoney.moneta.FastMoney;
import pl.training.shop.commons.money.LocalMoney;
import pl.training.shop.payments.application.PaymentRequest;
import pl.training.shop.payments.domain.Payment;
import pl.training.shop.payments.domain.PaymentStatus;

import java.time.Instant;

import static java.util.Collections.emptyMap;

public class PaymentsFixtures {

    public static final FastMoney MONEY = LocalMoney.of(1_000);
    public static final String PAYMENT_ID = "7e098f3d-076c-4ab8-9c74-7d1935efb501";
    public static final Instant TIMESTAMP = Instant.now();
    public static final PaymentRequest validPaymentRequest = new PaymentRequest(MONEY, emptyMap());
    public static final Payment expectedPayment = Payment.builder()
            .id(PAYMENT_ID)
            .value(MONEY)
            .properties(emptyMap())
            .timestamp(TIMESTAMP)
            .status(PaymentStatus.STARTED)
            .build();

}
