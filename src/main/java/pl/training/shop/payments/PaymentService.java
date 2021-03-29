package pl.training.shop.payments;

import lombok.extern.java.Log;

import java.time.Instant;

@Log
public class PaymentService {

    private static final String LOG_FORMAT = "A new payment of %s has been initiated";

    private final UUIDPaymentIdGenerator paymentIdGenerator = new UUIDPaymentIdGenerator();
    private final InMemoryPaymentRepository paymentRepository = new InMemoryPaymentRepository();

    public Payment process(PaymentRequest paymentRequest) {
        validate(paymentRequest);
        var payment = Payment.builder()
                .id(paymentIdGenerator.getNext())
                .value(paymentRequest.getValue())
                .properties(paymentRequest.getProperties())
                .timestamp(Instant.now())
                .status(PaymentStatus.STARTED)
                .build();
        log.info(String.format(LOG_FORMAT, payment.getValue()));
        return paymentRepository.save(payment);
    }

    private void validate(PaymentRequest paymentRequest) {
        if (paymentRequest.getValue().isNegativeOrZero()) {
            throw new InvalidPaymentRequest();
        }
    }

    public Payment findById(String id) {
        return paymentRepository.findById(id)
                .orElseThrow(PaymentNotFoundException::new);
    }

}
