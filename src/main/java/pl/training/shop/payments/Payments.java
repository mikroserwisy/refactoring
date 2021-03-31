package pl.training.shop.payments;

public interface Payments {

    Payment process(PaymentRequest paymentRequest);

    Payment findById(String id);

}
