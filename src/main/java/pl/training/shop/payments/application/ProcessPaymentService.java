package pl.training.shop.payments.application;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.training.shop.payments.ports.input.PaymentProcess;
import pl.training.shop.payments.ports.output.events.PaymentsEventEmitter;
import pl.training.shop.payments.ports.output.providers.TimeProvider;
import pl.training.shop.payments.domain.Payment;
import pl.training.shop.payments.domain.PaymentStatus;
import pl.training.shop.payments.ports.input.ProcessPaymentUseCase;
import pl.training.shop.payments.ports.output.persistence.PaymentUpdates;

import javax.inject.Inject;
import javax.transaction.Transactional;

@Transactional
@RequiredArgsConstructor
@NoArgsConstructor
public class ProcessPaymentService implements ProcessPaymentUseCase {

    @Inject
    @NonNull
    private PaymentIdGenerator paymentIdGenerator;
    @Inject
    @NonNull
    private PaymentUpdates paymentUpdates;
    @Inject
    @NonNull
    private TimeProvider timeProvider;
    @Inject
    @NonNull
    private PaymentsEventEmitter paymentsEventEmitter;

    @PaymentProcess
    @Override
    public Payment process(PaymentRequest paymentRequest) {
        var payment = paymentUpdates.save(createPayment(paymentRequest));
        paymentsEventEmitter.emit(payment);
        return payment;
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

}
