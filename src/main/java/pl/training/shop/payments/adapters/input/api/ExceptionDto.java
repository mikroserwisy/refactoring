package pl.training.shop.payments.adapters.input.api;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExceptionDto {

    private LocalDateTime timestamp;
    private String description;

}
