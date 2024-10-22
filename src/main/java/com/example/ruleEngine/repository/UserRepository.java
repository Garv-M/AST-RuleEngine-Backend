package com.example.ruleEngine.repository;

import com.example.ruleEngine.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findAll(); // Method to retrieve all users from the database
}