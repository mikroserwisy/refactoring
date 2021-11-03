package pl.sages.platform.projects;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.sages.platform.accounts.Account;
import pl.sages.platform.accounts.AccountService;

import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class WorkspacePathResolver {

    private static final Path WORKSPACES_FOLDER = Path.of("workspaces");

    @NonNull
    private final AccountService accountService;
    @Value("${platform.storage-path}")
    @Setter
    private Path storagePath;

    public Path getPath() {
        Account account = accountService.getActiveAccount();
        return storagePath.resolve(WORKSPACES_FOLDER).resolve(account.getId().toString());
    }

}
