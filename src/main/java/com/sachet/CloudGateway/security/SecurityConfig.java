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

  public SecurityConfig(JwtServerAuthenticationConvertor convertor) {
    this.convertor = convertor;
  }

  @Bean
  public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
    http
        .authorizeExchange(auth -> {
          auth.pathMatchers("/api/v1/auth/login").permitAll();
          auth.pathMatchers("/api/v1/auth/signup").permitAll();
          auth.anyExchange().authenticated();
        })
        .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .securityContextRepository(convertor);
    return http.build();
  }

}
