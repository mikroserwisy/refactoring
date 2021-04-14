package pl.training.shop.payments.commons;

import pl.training.shop.payments.ports.output.providers.TimeProvider;

import javax.enterprise.context.ApplicationScoped;
import java.time.Instant;

@ApplicationScoped
public class TimeProviderStub implements TimeProvider {

    @Override
    public Instant getTimestamp() {
        return PaymentsFixtures.TIMESTAMP;
    }

}
