package pl.training.shop.payments.adapters.input.api;

import lombok.Data;

import java.util.Map;

@Data
public class PaymentRequestDto {

    private String value;
    private Map<String, String> properties;

}
