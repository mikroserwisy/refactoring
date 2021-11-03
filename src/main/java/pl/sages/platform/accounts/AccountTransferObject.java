package pl.sages.platform.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access;

@Data
public class AccountTransferObject {

    private String email;
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;
    @JsonProperty(access = Access.READ_ONLY)
    private Set<String> roles = new HashSet<>();

}
