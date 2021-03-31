package pl.training.shop.payments.application;

import lombok.Setter;
import pl.training.shop.payments.domain.Payment;
import pl.training.shop.payments.ports.input.GetPaymentUseCase;
import pl.training.shop.payments.ports.output.persistence.FindPayment;

import javax.inject.Inject;
import javax.transaction.Transactional;

@Transactional
public class GetPaymentService implements GetPaymentUseCase {

    @Inject
    @Setter
    private FindPayment paymentRepository;

    @Override
    public Payment findById(String id) {
        return paymentRepository.findById(id)
                .orElseThrow(PaymentNotFoundException::new);
    }

}
