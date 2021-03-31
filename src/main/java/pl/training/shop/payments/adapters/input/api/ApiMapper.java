package pl.training.shop.payments.adapters.input.api;

import pl.training.shop.payments.application.PaymentRequest;
import pl.training.shop.payments.domain.Payment;

public interface ApiMapper {

    PaymentRequest toDomain(PaymentRequestDto paymentRequestDto);

    PaymentDto toDto(Payment payment);

}
