package pl.training.shop;

import lombok.extern.java.Log;
import pl.training.shop.payments.LocalMoney;
import pl.training.shop.payments.PaymentRequest;
import pl.training.shop.payments.PaymentService;

import static java.util.Collections.emptyMap;

@Log
public class Application {

    public static void main(String[] args) {
        var paymentService = new PaymentService();
        var paymentRequest = new PaymentRequest(LocalMoney.of(1_000), emptyMap());
        var payment = paymentService.process(paymentRequest);
        log.info(payment.toString());
    }

}
