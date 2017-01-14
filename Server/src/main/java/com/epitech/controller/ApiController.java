package com.epitech.controller;

import com.epitech.JwtUtils;
import com.epitech.model.Feed;
import com.epitech.model.Group;
import com.epitech.model.User;
import com.epitech.model.UserGroups;
import com.epitech.model.requests.NewGroupRequest;
import com.epitech.repository.UserGroupsRepository;
import com.epitech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private UserGroupsRepository userGroupsRepository;

    @Autowired
    UserRepository userRepository;

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    public ResponseEntity<List<Group>> getGroups(HttpServletRequest request) {

        String header = request.getHeader("Authorization");
        String token = header.substring("Bearer ".length());

        String username;
        try {
            username = JwtUtils.getUsername(JwtUtils.parse(token));
        } catch (ParseException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        User user = userRepository.findByEmail(username);
        UserGroups userGroups = userGroupsRepository.findByUserId(user.getId());

        return new ResponseEntity<>(userGroups.getGroups(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/groups", method = RequestMethod.PUT)
    public ResponseEntity<List<Group>> addGroup(HttpServletRequest request, @RequestBody NewGroupRequest body) {

        String header = request.getHeader("Authorization");
        String token = header.substring("Bearer ".length());

        String username;
        try {
            username = JwtUtils.getUsername(JwtUtils.parse(token));
        } catch (ParseException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String newGroupName = body.getGroup();

        if (newGroupName == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Getting user and its groups
        User user = userRepository.findByEmail(username);
        UserGroups userGroups = userGroupsRepository.findByUserId(user.getId());

        // Creating new group and add it to its groups
        List<Group> groups = userGroups.getGroups();
        Group newGroup = new Group(newGroupName, new ArrayList<Feed>());

        groups.add(newGroup);
        userGroups.setGroups(groups);

        // Save userGroups in db
        userGroupsRepository.save(userGroups);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
