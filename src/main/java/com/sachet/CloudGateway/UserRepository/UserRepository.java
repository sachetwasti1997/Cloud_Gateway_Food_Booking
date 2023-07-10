package com.sachet.CloudGateway.UserRepository;

import com.sachet.CloudGateway.dto.UserDto;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserRepository {
    private final ReactiveMongoTemplate template;
    public UserRepository(ReactiveMongoTemplate template) {
        this.template = template;
    }
    public Mono<UserDto> find(Query query) {
        return template.find(query, UserDto.class).elementAt(0);
    }
}
