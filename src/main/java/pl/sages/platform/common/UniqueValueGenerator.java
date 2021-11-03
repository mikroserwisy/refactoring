package pl.sages.platform.common;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UniqueValueGenerator {

    public String nextValue() {
        return UUID.randomUUID().toString();
    }

}
