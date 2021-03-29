package pl.training.shop;

import lombok.extern.java.Log;
import pl.training.shop.commons.LocalMoney;
import pl.training.shop.commons.SystemTimeProvider;
import pl.training.shop.payments.*;

import static java.util.Collections.emptyMap;

@Log
public class Application {

    public static void main(String[] args) {
        var paymentService = new PaymentService(new UUIDPaymentIdGenerator(), new InMemoryPaymentRepository(), new SystemTimeProvider());
        var payments = new PaymentsLoggingProxy(paymentService);
        var paymentRequest = new PaymentRequest(LocalMoney.of(1_000), emptyMap());
        var payment = payments.process(paymentRequest);
        log.info(payment.toString());
    }

}
