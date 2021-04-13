package pl.training.shop.payments;

import javax.ejb.Local;

@Local
public interface Payments {

    Payment process(PaymentRequest paymentRequest);

    Payment findById(String id);

}
