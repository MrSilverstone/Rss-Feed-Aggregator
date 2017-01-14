package com.epitech.controller;

import com.epitech.JwtUtils;
import com.epitech.model.Feed;
import com.epitech.model.Group;
import com.epitech.model.User;
import com.epitech.model.UserGroups;
import com.epitech.model.requests.NewFeedRequest;
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
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private UserGroupsRepository userGroupsRepository;

    @Autowired
    UserRepository userRepository;

    /**
     *  Validate the request with the token in the header
     *  @return NULL if missing or invalid token, otherwise return the username corresponding to the token
     * */
    private String validateRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.substring("Bearer ".length());

        try {
            return JwtUtils.getUsername(JwtUtils.parse(token));
        } catch (ParseException e) {
            return null;
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    public ResponseEntity<List<Group>> getGroups(HttpServletRequest request) {

        String username = validateRequest(request);

        if (username == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        User user = userRepository.findByEmail(username);
        UserGroups userGroups = userGroupsRepository.findByUserId(user.getId());

        return new ResponseEntity<>(userGroups.getGroups(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/groups", method = RequestMethod.PUT)
    public ResponseEntity<List<Group>> addGroup(HttpServletRequest request, @RequestBody NewGroupRequest body) {

        String username = validateRequest(request);

        if (username == null) {
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

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/feeds", method = RequestMethod.PUT)
    public ResponseEntity<List<Group>> addGroup(HttpServletRequest request, @RequestBody NewFeedRequest body) {

        String username = validateRequest(request);

        if (username == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String feedName = body.getFeed();
        String groupName = body.getGroup();

        if (feedName == null || groupName == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Getting user and its groups
        User user = userRepository.findByEmail(username);
        UserGroups userGroups = userGroupsRepository.findByUserId(user.getId());

        // Creating new feed and add it in the corresponding group
        List<Group> groups = userGroups.getGroups();
        Stream<Group> groupStream = groups.stream();

        List<Group> resultGroups = groupStream
                // Filter stream to get the group corresponding to groupName
                .filter(group -> group.getName().equals(groupName))

                // Adding the new Feed to this group
                .map(group -> {
                    List<Feed> feeds = group.getFeeds();
                    Feed newFeed = new Feed(feedName, new ArrayList<>());

                    feeds.add(newFeed);
                    group.setFeeds(feeds);

                    return group;
                })

                // Converting the result Stream to a List
                .collect(Collectors.toList());

        userGroups.setGroups(resultGroups);

        // Save userGroups in db
        userGroupsRepository.save(userGroups);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
