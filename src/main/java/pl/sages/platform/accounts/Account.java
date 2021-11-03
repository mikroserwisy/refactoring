package pl.sages.platform.accounts;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.sages.platform.products.Product;
import pl.sages.platform.products.ProductElementType;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.*;

@Table(indexes = {
        @Index(name = "account_email_index", columnList = "email", unique = true)
})
@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Account implements UserDetails {

    @GeneratedValue
    @Id
    private Long id;
    @Email
    @Column(unique = true)
    @NonNull
    private String email;
    @Column(nullable = false)
    @NonNull
    private String password;
    private String firstName;
    private String lastName;
    private String language;
    @Column(nullable = false)
    private boolean enabled;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
    @ManyToMany
    private Set<Product> products;
    @Column(name = "active_element_id")
    private Long activeElementId;
    @Enumerated(EnumType.STRING)
    @Column(name = "active_element_type")
    private ProductElementType activeElementType;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

    @Override
    public String toString() {
        return email;
    }

}
