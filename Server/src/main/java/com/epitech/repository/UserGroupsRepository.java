package com.epitech.repository;

import com.epitech.model.UserGroups;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface UserGroupsRepository extends MongoRepository<UserGroups, String> {

    UserGroups findByUserId(String userId);

}
