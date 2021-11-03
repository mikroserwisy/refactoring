package pl.sages.platform.repositories;

import lombok.Data;

@Data
public class RepositoryTransferObject {

    private String remoteUrl;
    private String username;
    private String password;

}
