package pl.training.shop.payments.adapters.output.providers;

import pl.training.shop.payments.ports.output.providers.TimeProvider;

import javax.enterprise.context.ApplicationScoped;
import java.time.Instant;

@ApplicationScoped
public class SystemTimeProvider implements TimeProvider {

    @Override
    public Instant getTimestamp() {
        return Instant.now();
    }

}
