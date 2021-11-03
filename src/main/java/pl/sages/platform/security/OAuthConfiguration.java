package pl.sages.platform.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.JdbcClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class OAuthConfiguration {

    @Value("classpath:token-store-schema.sql")
    private Resource schemaScript;

    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource, DatabasePopulator databasePopulator) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator);
        return initializer;
    }

    @Bean
    public DatabasePopulator databasePopulator() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaScript);
        return populator;
    }

    @Bean
    public TokenStore tokenStore(DataSource dataSource) {
        return new JdbcTokenStore(dataSource);
    }

    @EnableAuthorizationServer
    @Configuration
    public static class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

        public static final String CLIENT_ID = "platform";
        public static final String TOKEN_TYPE = "bearer";

        @Autowired
        private AuthenticationManager authenticationManagerBean;
        @Autowired
        private PasswordEncoder passwordEncoder;
        @Autowired
        private TokenStore tokenStore;
        @Autowired
        private SecurityService securityService;
        @Autowired
        public DataSource dataSource;

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
            endpoints.userDetailsService(securityService).tokenStore(tokenStore).authenticationManager(authenticationManagerBean);
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) {
            security.allowFormAuthenticationForClients().passwordEncoder(passwordEncoder);
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            JdbcClientDetailsServiceBuilder detailsServiceBuilder = clients.jdbc(dataSource);
            try {
                new JdbcClientDetailsService(dataSource).loadClientByClientId("platform");
            } catch (Exception ignored) {
                log.info("Creating oauth client configuration");
                detailsServiceBuilder
                        .withClient(CLIENT_ID)
                        .authorizedGrantTypes("password", "refresh_token")
                        .scopes("web")
                        .accessTokenValiditySeconds(10 * 60)
                        .refreshTokenValiditySeconds(60 * 60);
            }
        }

    }

    @EnableResourceServer
    @Configuration
    public static class ResourceServer extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/api/v1/accounts").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/v1/accounts/blocked").permitAll()
                    .regexMatchers(HttpMethod.PATCH, "\\/api\\/v1\\/accounts\\/\\d+\\?token=.*").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/v1/products/active").hasRole("USER")
                    .antMatchers("/api/v1/repositories/**").hasRole("ADMIN")
                    .antMatchers("/api/v1/products/**").hasRole("ADMIN")
                    .antMatchers("/api/v1/tests/**").hasRole("ADMIN")
                    .regexMatchers(HttpMethod.GET, "\\/api\\/v1\\/projects\\?.*").hasRole("ADMIN")
                    .antMatchers("/api/v1/**").hasRole("USER");
        }

    }

}
