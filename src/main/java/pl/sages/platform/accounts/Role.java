package pl.sages.platform.accounts;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Table(indexes = {
        @Index(name = "role_name_index",  columnList="name", unique = true)
})
@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {

    public static final String USER = "ROLE_USER";
    public static final String ADMIN = "ROLE_ADMIN";

    @GeneratedValue
    @Id
    private Long id;
    @Column(unique = true)
    @NonNull
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }

}
