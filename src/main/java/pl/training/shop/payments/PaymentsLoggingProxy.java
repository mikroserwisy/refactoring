
package pl.training.shop.payments;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
public class PaymentsLoggingProxy implements Payments {

    private static final String LOG_FORMAT = "A new payment of %s has been created";

    private final Payments payments;

    @Override
    public Payment process(PaymentRequest paymentRequest) {
        var payment = payments.process(paymentRequest);
        log.info(String.format(LOG_FORMAT, payment.getValue()));
        return payment;
    }

    @Override
    public Payment findById(String id) {
        return payments.findById(id);
    }

}
