package pl.training.shop.payments.adapters.input.logging;

import lombok.extern.java.Log;
import pl.training.shop.payments.domain.Payment;
import pl.training.shop.payments.ports.input.LogPayment;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@LogPayment
@Interceptor
@Log
public class PaymentConsoleLogger {

    private static final String LOG_FORMAT = "A new payment of %s has been created";

    @AroundInvoke
    public Object log(InvocationContext invocationContext) throws Exception {
        var payment = (Payment) invocationContext.proceed();
        log.info(String.format(LOG_FORMAT, payment.getValue()));
        return payment;
    }

}
