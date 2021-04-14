package pl.training.shop.payments.application;

import lombok.Setter;
import pl.training.shop.payments.domain.Payment;
import pl.training.shop.payments.ports.input.GetPaymentUseCase;
import pl.training.shop.payments.ports.output.persistence.PaymentQueries;

import javax.inject.Inject;
import javax.transaction.Transactional;

@Transactional
public class GetPaymentService implements GetPaymentUseCase {

    @Inject
    @Setter
    private PaymentQueries paymentQueries;

    @Override
    public Payment findById(String id) {
        return paymentQueries.findById(id)
                .orElseThrow(PaymentNotFoundException::new);
    }

}
