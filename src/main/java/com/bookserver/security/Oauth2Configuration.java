package com.bookserver.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
public class Oauth2Configuration {
    public static final String RESOURCE_ID = "books";

    @EnableResourceServer
    protected static class OAuth2ResourceServer extends ResourceServerConfigurerAdapter {

        @Autowired
        private UserService userService;

        @Bean
        public RemoteTokenServices remoteTokenServices() {
            RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
            remoteTokenServices.setClientId("resource-server");
            remoteTokenServices.setClientSecret("abc");
            remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:8080/oauth/check_token");
            remoteTokenServices.setAccessTokenConverter(accessTokenConverter());
            return remoteTokenServices;
        }

        @Bean
        public AccessTokenConverter accessTokenConverter() {
            DefaultAccessTokenConverter converter = new DefaultAccessTokenConverter();
            converter.setUserTokenConverter(userAuthenticationConverter());
            return converter;
        }

        @Bean
        public UserAuthenticationConverter userAuthenticationConverter() {
            DefaultUserAuthenticationConverter converter = new DefaultUserAuthenticationConverter();
            converter.setUserDetailsService(this.userService);
            return converter;
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.resourceId(RESOURCE_ID);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                .authorizeRequests()
                .anyRequest().authenticated().and()
                .requestMatchers()
                .antMatchers("/v2/bookserver/user").and()
                .cors();
        }
    }

    @EnableAuthorizationServer
    protected static class OAuth2AuthorizationServer extends AuthorizationServerConfigurerAdapter {

        // Necessário apenas para utilização do grant type Resource Owner Password Credentials
        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private ClientDetailsService clientDetailsService;

        @Autowired
        @Qualifier("oauthDataSource")
        private DataSource dataSource;

        @Bean
        public TokenStore tokenStore() {
            return new JdbcTokenStore(this.dataSource);
        }

        @Bean
        public ApprovalStore approvalStore() {
            return new JdbcApprovalStore(this.dataSource);
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) {
            security.checkTokenAccess("hasAuthority('introspection')");
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.jdbc(this.dataSource);
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
            DefaultOAuth2RequestFactory requestFactory = new DefaultOAuth2RequestFactory(clientDetailsService);
            requestFactory.setCheckUserScopes(true);

            endpoints
                .authenticationManager(this.authenticationManager)
                .requestFactory(requestFactory)
                .approvalStore(this.approvalStore())
                .tokenStore(this.tokenStore());
        }
    }

}
