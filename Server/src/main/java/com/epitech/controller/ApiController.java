package com.epitech.controller;

import com.epitech.JwtUtils;
import com.epitech.model.Feed;
import com.epitech.model.Group;
import com.epitech.model.User;
import com.epitech.model.UserGroups;
import com.epitech.model.requests.NewFeedRequest;
import com.epitech.model.requests.NewGroupRequest;
import com.epitech.model.requests.ReadItemRequest;
import com.epitech.model.requests.Response;
import com.epitech.repository.UserGroupsRepository;
import com.epitech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
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
     *  @param request on whom to make validation
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

    /**
     *  Get user groups
     *  @param request HTTP GET request, need Token in header Authorization
     *  @return OK: JSON object of groups, otherwise returns BAD_REQUEST
     *  @see UserGroups#getGroups() for response structure
     * */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    public ResponseEntity<List<Group>> getGroups(HttpServletRequest request) {

        String username = validateRequest(request);

        if (username == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(username);
        UserGroups userGroups = userGroupsRepository.findByUserId(user.getId());

        List<Group> groups;

        if (userGroups == null) {
            groups = new ArrayList<>();
        } else{
            groups = userGroups.getGroups();
        }

        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    /**
     *  Add new group to user groups
     *  @param request HTTP PUT request, need Token in header Authorization
     *  @param body Body of the request
     *  @return OK, BAD_REQUEST when body or request is invalid
     *  @see com.epitech.model.requests.NewGroupRequest for body structure
     * */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/groups", method = RequestMethod.PUT)
    public ResponseEntity<?> addGroup(HttpServletRequest request, @RequestBody NewGroupRequest body) {

        String username = validateRequest(request);
        String newGroupName = body.getGroup();

        if (username == null || newGroupName == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Getting user and its groups
        User user = userRepository.findByEmail(username);
        UserGroups userGroups = userGroupsRepository.findByUserId(user.getId());

        // Creating new group and add it to its groups
        List<Group> groups = userGroups.getGroups();
        Stream<Group> streamGroup = groups.stream();
        Stream<Group> existingGroupsForGroupName = streamGroup
                .filter(group -> group.getName().equals(newGroupName));

        if (existingGroupsForGroupName.count() > 0) {
            Response resp = new Response(false, "Group already exists.");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        Group newGroup = new Group(newGroupName, new ArrayList<>());

        groups.add(newGroup);
        userGroups.setGroups(groups);

        // Save userGroups in db
        userGroupsRepository.save(userGroups);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *  Add new feed to group in user groups
     *  @param request HTTP PUT request, need Token in header Authorization
     *  @param body Body of the request
     *  @return OK, BAD_REQUEST when body or request is invalid
     *  @see com.epitech.model.requests.NewFeedRequest for body structure
     * */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/feeds", method = RequestMethod.PUT)
    public ResponseEntity<Void> addGroup(HttpServletRequest request, @RequestBody NewFeedRequest body) {

        String username = validateRequest(request);
        String feedName = body.getFeed();
        String groupName = body.getGroup();

        if (username == null || feedName == null || groupName == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Getting user and its groups
        User user = userRepository.findByEmail(username);
        UserGroups userGroups = userGroupsRepository.findByUserId(user.getId());

        // Creating new feed and add it in the corresponding group
        List<Group> groups = userGroups.getGroups();
        Stream<Group> groupStream = groups.stream();


        List<Group> resultGroups = groupStream
                .map(group -> {
                    if (group.getName().equals(groupName)) {
                        List<Feed> feeds = group.getFeeds();
                        Feed newFeed = new Feed(feedName, new ArrayList<>());

                        feeds.add(newFeed);
                        group.setFeeds(feeds);
                    }

                    return group;
                })

                // Converting the result Stream to a List
                .collect(Collectors.toList());

        userGroups.setGroups(resultGroups);

        // Save userGroups in db
        userGroupsRepository.save(userGroups);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *  Add read item to its corresponding feed
     *  @param request HTTP PUT request, need Token in header Authorization
     *  @param body Body of the request
     *  @return OK, BAD_REQUEST when body or request is invalid
     *  @see com.epitech.model.requests.ReadItemRequest for body structure
     * */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/feeds/items", method = RequestMethod.PUT)
    public ResponseEntity<Void> addGroup(HttpServletRequest request, @RequestBody ReadItemRequest body) {
        String username = validateRequest(request);
        String feedName = body.getUrl();
        String identifier = body.getIdentifier();
        String groupName = body.getGroupName();

        if (username == null || feedName == null || identifier == null || groupName == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Getting user and its groups
        User user = userRepository.findByEmail(username);
        UserGroups userGroups = userGroupsRepository.findByUserId(user.getId());
        List<Group> groups = userGroups.getGroups();

        Stream<Group> streamGroup = groups.stream();
        Stream<Group> streamResult = streamGroup
                .map(group -> {
                    // Get group for groupName
                    if (group.getName().equals(groupName)) {
                        Stream<Feed> feedStream = group.getFeeds().stream();
                        Stream<Feed> feedStreamResult = feedStream.map(feed -> {
                            // Get feed for feedName and add identifier
                            if (feed.getUrl().equals(feedName)) {
                                List<String> readItems = feed.getReadItems();

                                readItems.add(identifier);
                                feed.setReadItems(readItems);
                            }

                            return feed;
                        });

                        List<Feed> updatedFeeds = feedStreamResult.collect(Collectors.toList());
                        group.setFeeds(updatedFeeds);
                    }

                    return group;
                });

        // Save in db
        List<Group> updatedGroups = streamResult.collect(Collectors.toList());
        userGroups.setGroups(updatedGroups);
        userGroupsRepository.save(userGroups);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *  Delete all groups of user
     *  @param request HTTP DELETE request, need Token in header Authorization
     *  @return OK, BAD_REQUEST when body or request is invalid
     * */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/groups", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteGroups(HttpServletRequest request) {
        String username = validateRequest(request);

        if (username == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Getting user and its groups
        User user = userRepository.findByEmail(username);
        UserGroups userGroups = userGroupsRepository.findByUserId(user.getId());

        // Resetting groups
        userGroups.setGroups(new ArrayList<>());
        userGroupsRepository.save(userGroups);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
