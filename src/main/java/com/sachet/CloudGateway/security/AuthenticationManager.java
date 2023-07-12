package com.sachet.CloudGateway.security;

import com.sachet.CloudGateway.UserRepository.UserRepository;
import com.sachet.CloudGateway.service.UserService;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

  private final JwtUtil jwtUtil;
  private final UserService userService;

  public AuthenticationManager(JwtUtil jwtUtil, UserService userService) {
    this.jwtUtil = jwtUtil;
    this.userService = userService;
  }

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {
    String token = authentication.getCredentials().toString();
    String email = jwtUtil.extractUsername(token);
    return userService.searchUser(email)
        .flatMap(userDto -> {
          if (jwtUtil.validateToken(token, userDto)) {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            userDto.getRoles().forEach(
                role -> authorities.add(new SimpleGrantedAuthority(role))
            );
            return Mono.just(new UsernamePasswordAuthenticationToken(userDto.getEmail(), token, authorities));
          } else {
            return Mono.empty();
          }
        });
  }
}
