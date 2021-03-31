package pl.training.shop.payments.adapters.output.persistence;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.training.shop.payments.domain.Payment;

@Mapper(componentModel = "cdi")
public interface PersistenceMapper {

    @Mapping(source = "properties", target = "additionalProperties")
    PaymentEntity toEntity(Payment payment);

    @InheritInverseConfiguration
    Payment toDomain(PaymentEntity paymentEntity);

}
