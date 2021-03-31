package pl.training.shop.payments.ports.output.providers;

import java.time.Instant;

public interface TimeProvider {

    Instant getTimestamp();

}
