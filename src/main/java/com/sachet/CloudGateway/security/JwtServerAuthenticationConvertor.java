package com.sachet.CloudGateway.security;

import com.sachet.CloudGateway.UserRepository.UserRepository;
import com.sachet.CloudGateway.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class JwtServerAuthenticationConvertor implements ServerSecurityContextRepository {
  private final UserService userService;
  private final JwtUtil jwtUtil;

  public JwtServerAuthenticationConvertor(UserService userService, JwtUtil jwtUtil) {
    this.userService = userService;
    this.jwtUtil = jwtUtil;
  }

  @Override
  public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
    return Mono.empty();
  }

  @Override
  public Mono<SecurityContext> load(ServerWebExchange exchange) {
    String bearer = "Bearer ";
    return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
        .filter(b -> b.startsWith(bearer))
        .map(token -> token.substring(bearer.length()))
        .flatMap(token -> {
          String email = jwtUtil.extractUsername(token);
          return userService.searchUser(email)
              .map(userDto -> {
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                userDto.getRoles().forEach(
                    role -> authorities.add(new SimpleGrantedAuthority(role))
                );
                if (jwtUtil.validateToken(token, userDto)) {
                  return new UsernamePasswordAuthenticationToken(
                      userDto.getEmail(),
                      token,
                      authorities
                  );
                } else {
                  return null;
                }
              });
        })
        .map(SecurityContextImpl::new);
  }
}
