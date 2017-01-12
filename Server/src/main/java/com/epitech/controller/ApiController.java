package com.epitech.controller;

import com.epitech.entity.Group;
import com.epitech.entity.UserGroups;
import com.epitech.repository.UserGroupsRepository;
import com.epitech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private UserGroupsRepository userGroupsRepository;

    @RequestMapping(value = "/groups")
    public ResponseEntity<List<Group>> getGroups() {

        UserGroups userGroups = userGroupsRepository.getUserGroupsByUserId("5877a854f8ab1e53f7f4a1d7");
        return new ResponseEntity<>(userGroups.getGroups(), HttpStatus.OK);
    }
}
