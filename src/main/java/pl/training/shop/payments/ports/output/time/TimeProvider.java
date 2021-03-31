package pl.training.shop.payments.ports.output.time;

import java.time.Instant;

public interface TimeProvider {

    Instant getTimestamp();

}
