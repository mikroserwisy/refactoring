package pl.sages.platform.common;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class ResourceUri {

    private static final String SEPARATOR = "/";

    @Value("${platform.api-prefix}")
    @Setter
    private String apiPrefix;

    public URI from(String path, Object segment) {
        return URI.create(apiPrefix + SEPARATOR + path + SEPARATOR + segment);
    }

}
