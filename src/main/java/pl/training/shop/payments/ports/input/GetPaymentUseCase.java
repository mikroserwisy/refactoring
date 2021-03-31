package pl.training.shop.payments.ports.input;

import pl.training.shop.payments.domain.Payment;

public interface GetPaymentUseCase {

    Payment findById(String id);

}
