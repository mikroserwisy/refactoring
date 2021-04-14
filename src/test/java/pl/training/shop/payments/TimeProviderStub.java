package pl.training.shop.payments;

import pl.training.shop.commons.TimeProvider;

import javax.enterprise.context.ApplicationScoped;
import java.time.Instant;

@ApplicationScoped
public class TimeProviderStub implements TimeProvider {

    @Override
    public Instant getTimestamp() {
        return PaymentsFixtures.TIMESTAMP;
    }

}
