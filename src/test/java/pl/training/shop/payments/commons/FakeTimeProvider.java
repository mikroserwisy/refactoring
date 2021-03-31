package pl.training.shop.payments.commons;

import pl.training.shop.payments.ports.output.providers.TimeProvider;

import java.time.Instant;

public class FakeTimeProvider implements TimeProvider {

    private static final Instant TIMESTAMP = Instant.now();

    @Override
    public Instant getTimestamp() {
        return TIMESTAMP;
    }

}
