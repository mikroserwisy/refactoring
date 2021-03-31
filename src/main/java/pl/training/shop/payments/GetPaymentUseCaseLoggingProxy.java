
package pl.training.shop.payments;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import pl.training.shop.payments.application.PaymentRequest;
import pl.training.shop.payments.ports.input.GetPaymentUseCase;
import pl.training.shop.payments.domain.Payment;

@Log
@RequiredArgsConstructor
public class GetPaymentUseCaseLoggingProxy  {

    private static final String LOG_FORMAT = "A new payment of %s has been created";

    private final GetPaymentUseCase getPaymentUseCase;

    public Payment process(PaymentRequest paymentRequest) {
      //  var payment = getPaymentUseCase.process(paymentRequest);
      //  log.info(String.format(LOG_FORMAT, payment.getValue()));
        return new Payment();
    }


}
