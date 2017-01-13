package com.epitech.repository;


import com.epitech.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String>{

    User findByEmail(String email);
}
