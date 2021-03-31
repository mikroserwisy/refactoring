package pl.training.shop.payments.adapters.output.time;

import pl.training.shop.payments.ports.output.time.TimeProvider;

import javax.enterprise.context.ApplicationScoped;
import java.time.Instant;

@ApplicationScoped
public class SystemTimeProvider implements TimeProvider {

    @Override
    public Instant getTimestamp() {
        return Instant.now();
    }

}
