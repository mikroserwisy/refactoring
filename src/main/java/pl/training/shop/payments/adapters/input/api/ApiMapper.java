package pl.training.shop.payments.adapters.input.api;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import pl.training.shop.commons.money.FastMoneyMapper;
import pl.training.shop.payments.application.PaymentRequest;
import pl.training.shop.payments.domain.Payment;

@Mapper(componentModel = "cdi", uses = FastMoneyMapper.class)
public interface ApiMapper {

    PaymentRequest toDomain(PaymentRequestDto paymentRequestDto);

    @InheritInverseConfiguration
    PaymentDto toDto(Payment payment);

}
