package pl.training.shop.commons.time;

import java.time.Instant;

public interface TimeProvider {

    Instant getTimestamp();

}
