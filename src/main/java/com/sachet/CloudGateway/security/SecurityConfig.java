package com.sachet.CloudGateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  private final JwtServerAuthenticationConvertor convertor;
  private final AuthenticationManager authenticationManager;

  public SecurityConfig(JwtServerAuthenticationConvertor convertor, AuthenticationManager authenticationManager) {
    this.convertor = convertor;
    this.authenticationManager = authenticationManager;
  }

  @Bean
  public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
    http
        .authorizeExchange(auth -> {
          auth.pathMatchers("/api/v1/auth/login").permitAll()
              .pathMatchers("/api/v1/auth/signup").permitAll()
              .anyExchange().authenticated();
        })
        .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .authenticationManager(authenticationManager)
        .securityContextRepository(convertor);
    return http.build();
  }

}
