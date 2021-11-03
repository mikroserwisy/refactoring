package pl.sages.platform.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import pl.sages.platform.accounts.AccountService;
import pl.sages.platform.common.configuration.TransactionalService;

@TransactionalService
@RequiredArgsConstructor
public class SecurityService  implements UserDetailsService {

    @NonNull
    private final AccountService accountService;
    @NonNull
    private final TokenStore tokenStore;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountService.getAccount(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account " + email + " not found"));
    }

    public void logout(String tokenValue) {
        OAuth2AccessToken token = tokenStore.readAccessToken(tokenValue);
        tokenStore.removeRefreshToken(token.getRefreshToken());
        tokenStore.removeAccessToken(token);
    }

}
