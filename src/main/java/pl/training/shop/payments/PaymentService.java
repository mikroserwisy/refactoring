package pl.training.shop.payments;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.training.shop.commons.TimeProvider;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
@RequiredArgsConstructor
@NoArgsConstructor
public class PaymentService implements Payments {

    @Inject
    @NonNull
    private PaymentIdGenerator paymentIdGenerator;
    @Inject
    @NonNull
    private PaymentRepository paymentRepository;
    @Inject
    @NonNull
    private TimeProvider timeProvider;

    @Override
    public Payment process(PaymentRequest paymentRequest) {
        var payment = createPayment(paymentRequest);
        return paymentRepository.save(payment);
    }

    private Payment createPayment(PaymentRequest paymentRequest) {
        return Payment.builder()
                .id(paymentIdGenerator.getNext())
                .value(paymentRequest.getValue())
                .properties(paymentRequest.getProperties())
                .timestamp(timeProvider.getTimestamp())
                .status(PaymentStatus.STARTED)
                .build();
    }

    @Override
    public Payment findById(String id) {
        return paymentRepository.findById(id)
                .orElseThrow(PaymentNotFoundException::new);
    }

}
