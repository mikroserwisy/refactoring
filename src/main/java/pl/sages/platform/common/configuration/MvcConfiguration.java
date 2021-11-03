package pl.sages.platform.common.configuration;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static java.util.Arrays.asList;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

@EnableSwagger2
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    @Value("${platform.allowed-origin}")
    @Setter
    private String allowedOrigin;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(basePackage("pl.sages.platform"))
                .build()
                .securitySchemes(asList(securityScheme()))
                .securityContexts(asList(securityContext()));
    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId("platform")
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }

    private SecurityScheme securityScheme() {
        GrantType grantType = new ResourceOwnerPasswordCredentialsGrant( "http://localhost:8000/oauth/token");
        SecurityScheme oauth = new OAuthBuilder().name("spring_oauth")
                .grantTypes(asList(grantType))
                .scopes(asList(scopes()))
                .build();
        return oauth;
    }

    private AuthorizationScope[] scopes() {
        AuthorizationScope[] scopes = { new AuthorizationScope("web", "") };
        return scopes;
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(asList(new SecurityReference("spring_oauth", scopes())))
                .forPaths(PathSelectors.regex("/.*"))
                .build();
    }

}
