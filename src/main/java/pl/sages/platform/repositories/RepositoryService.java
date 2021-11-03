package pl.sages.platform.repositories;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

@Service
public class RepositoryService {

    public void cloneRepository(Repository repository) {
        try {
            Git.cloneRepository()
                    .setURI(repository.getRemoteUrl())
                    .setDirectory(repository.getLocalPath().toFile())
                    .setCredentialsProvider(createCredentialsProviderForRepository(repository))
                    .call();
        } catch (Exception exception) {
            throw new CloneRepositoryFailedException();
        }
    }

    public void deleteRepository(Path repositoryPath) {
        try {
            Files.walk(repositoryPath)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (Exception exception) {
            throw new DeleteRepositoryFailedException();
        }
    }

    public void pullChanges(Repository repository) {
        try {
            Git.open(repository.getLocalPath().toFile())
                    .pull()
                    .setCredentialsProvider(createCredentialsProviderForRepository(repository))
                    .call();
        } catch (Exception exception) {
            throw new PullChangesFailedException();
        }
    }

    public void commitChanges(Repository repository, String commitMessage) {
        try {
            Git.open(repository.getLocalPath().toFile())
                    .commit()
                    .setMessage(commitMessage)
                    .call();
        } catch (Exception exception) {
            throw new CommitChangesFailedException();
        }
    }

    private UsernamePasswordCredentialsProvider createCredentialsProviderForRepository(Repository repository) {
        return new UsernamePasswordCredentialsProvider(repository.getUsername(), repository.getPassword());
    }

}
