package com.sachet.CloudGateway.service;

import com.sachet.CloudGateway.UserRepository.UserRepository;
import com.sachet.CloudGateway.dto.UserDto;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<UserDto> searchUser(String email) {
        Criteria criteria = Criteria.where("email").is(email);
        return userRepository.find(new Query().addCriteria(criteria));
    }
}
