package pl.training.shop.payments.adapters.input.api;

import lombok.Data;

import java.time.Instant;

@Data
public class PaymentDto {

    private String id;
    private Instant timestamp;

}
