package pl.sages.platform.repositories;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.util.Optional;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Repository {

    @NonNull
    private String remoteUrl;
    private Path localPath;
    private String username;
    private String password;

    public String getUsername() {
        return getOfNullable(username);
    }

    public String getPassword() {
        return getOfNullable(password);
    }

    private String getOfNullable(String value) {
        return Optional.ofNullable(value).orElse("");
    }

}
