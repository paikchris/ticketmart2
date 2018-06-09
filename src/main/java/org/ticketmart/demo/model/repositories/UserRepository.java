package org.ticketmart.demo.model.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.ticketmart.demo.model.User;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    public User findByFirstName(String firstName);

    public List<User> findByLastName(String lastName);

    public List<User> findByEmail(String email);


}