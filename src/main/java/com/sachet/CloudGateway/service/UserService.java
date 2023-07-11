package com.sachet.CloudGateway.service;

import com.sachet.CloudGateway.UserRepository.UserRepository;
import com.sachet.CloudGateway.dto.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> searchUser(String email) {
      System.out.println("Method called with "+email);
      return userRepository.findByEmail(email);
    }
}
