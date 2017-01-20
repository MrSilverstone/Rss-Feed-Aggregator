package com.epitech.controller;

import com.epitech.JwtUtils;
import com.epitech.business.UserGroupsBO;
import com.epitech.model.Feed;
import com.epitech.model.Group;
import com.epitech.model.User;
import com.epitech.model.UserGroups;
import com.epitech.model.requests.*;
import com.epitech.repository.UserGroupsRepository;
import com.epitech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private UserGroupsRepository userGroupsRepository;

    @Autowired
    private UserRepository userRepository;

    private UserGroupsBO userGroupsBO = new UserGroupsBO();

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
     *
     *  USER GROUPS REQUESTS :
     *  - Get groups
     *  - Add group
     *  - Delete all groups
     *  - Delete group
     * */

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
     *  @param groupName group name to add
     *  @return OK, BAD_REQUEST when request is invalid
     * */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/groups/{groupName}", method = RequestMethod.PUT)
    public ResponseEntity<?> addGroup(HttpServletRequest request, @PathVariable("groupName") String groupName) {

        String username = validateRequest(request);

        if (username == null || groupName == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Getting user and its groups
        User user = userRepository.findByEmail(username);
        UserGroups userGroups = userGroupsRepository.findByUserId(user.getId());

        Group newGroup = new Group(groupName, new ArrayList<>());

        if (!userGroupsBO.addGroup(userGroups, newGroup)) {
            Response resp = new Response(false, "Group already exists.");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        // Save userGroups in db
        userGroupsRepository.save(userGroups);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *  Delete all groups of user
     *  @param request HTTP DELETE request, need Token in header Authorization
     *  @return OK, BAD_REQUEST when request is invalid
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

    /**
     *  Delete a group in user groups
     *  @param request HTTP DELETE request, need Token in header Authorization
     *  @param groupName group name to delete
     *  @return OK, BAD_REQUEST when request is invalid
     * */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/groups/{groupName}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteGroup(HttpServletRequest request, @PathVariable("groupName") String groupName) {
        String username = validateRequest(request);

        if (username == null || groupName == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Getting user and its groups
        User user = userRepository.findByEmail(username);
        UserGroups userGroups = userGroupsRepository.findByUserId(user.getId());

        if (!userGroupsBO.deleteGroup(userGroups, groupName)) {
            Response resp = new Response(false, "Group does not exist.");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        userGroupsRepository.save(userGroups);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *
     *  FEEDS REQUESTS :
     *  - Add feed
     *  - Get feeds
     *  - Delete feed
     * */

    /**
     *  Delete body in user groups
     *  @param request HTTP GET request, need Token in header Authorization
     *  @param body request body
     *  @return OK or BAD_REQUEST when request is invalid (unknown group/feed)
     *  @see Response for response structure
     * */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/feeds", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteFeed(HttpServletRequest request, @RequestBody DeleteFeedBody body) {
        String username = validateRequest(request);
        String groupName = body.getGroupName();
        String feedName = body.getFeedName();

        if (username == null || groupName == null || feedName == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Getting user and its groups
        User user = userRepository.findByEmail(username);
        UserGroups userGroups = userGroupsRepository.findByUserId(user.getId());

        UserGroupsBO.ErrorType error = userGroupsBO.deleteFeed(userGroups, groupName, feedName);

        if (error == UserGroupsBO.ErrorType.GroupDoesNotExist) {
            Response resp = new Response(false, "Group name does not exist.");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        } else if (error == UserGroupsBO.ErrorType.FeedDoesNotExist) {
            Response resp = new Response(false, "Feed does not exist.");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        userGroupsRepository.save(userGroups);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *  Get feeds from group name in user groups
     *  @param request HTTP GET request, need Token in header Authorization
     *  @param groupName feed's group name to get
     *  @return OK or BAD_REQUEST when request is invalid (unknown group)
     *  @see Response for response structure
     * */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/feeds/{groupName}", method = RequestMethod.GET)
    public ResponseEntity<?> getFeed(HttpServletRequest request, @PathVariable("groupName") String groupName) {
        String username = validateRequest(request);

        if (username == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Getting user and its groups
        User user = userRepository.findByEmail(username);
        UserGroups userGroups = userGroupsRepository.findByUserId(user.getId());

        List<Feed> feeds = userGroupsBO.getFeeds(userGroups, groupName);

        if (feeds == null) {
            Response resp = new Response(false, "Group does not exist.");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(feeds, HttpStatus.OK);
    }



    /**
     *  Add new feed to group in user groups
     *  @param request HTTP PUT request, need Token in header Authorization
     *  @param body body of the request
     *  @return OK or BAD_REQUEST when body or request is invalid (unknown group/feed name, item already read)
     *  @see AddFeedBody for body structure
     *  @see Response for response structure
     * */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/feeds", method = RequestMethod.PUT)
    public ResponseEntity<?> addFeed(HttpServletRequest request, @RequestBody AddFeedBody body) {

        String username = validateRequest(request);
        String groupName = body.getGroupName();
        String feedName = body.getFeedName();

        if (username == null || groupName == null || feedName == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Getting user and its groups
        User user = userRepository.findByEmail(username);
        UserGroups userGroups = userGroupsRepository.findByUserId(user.getId());

        // Creating new feed and add it in the corresponding group
        Feed newFeed = new Feed(feedName, new ArrayList<>());

        UserGroupsBO.ErrorType error = userGroupsBO.addFeedToGroup(userGroups, newFeed, groupName);

        if (error == UserGroupsBO.ErrorType.GroupDoesNotExist) {
            Response resp = new Response(false, "Group name does not exist.");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        } else if (error == UserGroupsBO.ErrorType.FeedExists) {
            Response resp = new Response(false, "Feed already exists.");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        // Save userGroups in db
        userGroupsRepository.save(userGroups);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *
     *  Items requests :
     *
     *  - Delete all read items in feed
     *  - Add read item in feed
     *  - Get read items
     * */

    /**
     *  Delete read items from feed in user groups
     *  @param request HTTP PUT request, need Token in header Authorization
     *  @param body body of the request
     *  @return OK or BAD_REQUEST when request is invalid (unknown group/feed name)
     *  @see DeleteReadItemsBody for body structure
     *  @see Response for response structure
     * */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/items", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteReadItems(HttpServletRequest request, @RequestBody DeleteReadItemsBody body) {
        String username = validateRequest(request);
        String groupName = body.getGroupName();
        String feedName = body.getFeedName();

        if (username == null || groupName == null || feedName == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Getting user and its groups
        User user = userRepository.findByEmail(username);
        UserGroups userGroups = userGroupsRepository.findByUserId(user.getId());

        UserGroupsBO.ErrorType error = userGroupsBO.deleteReadItems(userGroups, groupName, feedName);

        if (error == UserGroupsBO.ErrorType.GroupDoesNotExist) {
            Response resp = new Response(false, "Group name does not exist.");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        } else if (error == UserGroupsBO.ErrorType.FeedDoesNotExist) {
            Response resp = new Response(false, "Feed does not exist.");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        userGroupsRepository.save(userGroups);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *  Add read item to its corresponding feed
     *  @param request HTTP PUT request, need Token in header Authorization
     *  @param body body of the request
     *  @return OK or BAD_REQUEST when request is invalid (unknown group/feed name, item already read)
     *  @see AddReadItemBody for body structure
     *  @see Response for response structure
     * */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/items", method = RequestMethod.PUT)
    public ResponseEntity<?> addReadItem(HttpServletRequest request, @RequestBody AddReadItemBody body) {
        String username = validateRequest(request);
        String groupName = body.getGroupName();
        String feedName = body.getFeedName();
        String identifier = body.getIdentifier();

        if (username == null || groupName == null || feedName == null || identifier == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Getting user and its groups
        User user = userRepository.findByEmail(username);
        UserGroups userGroups = userGroupsRepository.findByUserId(user.getId());

        UserGroupsBO.ErrorType errorType = userGroupsBO.addReadItemInFeed(userGroups,
                feedName, identifier, groupName);

        Response resp;

        switch (errorType) {
            case FeedDoesNotExist:
                resp = new Response(false, "Feed does not exist.");
                return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
            case GroupDoesNotExist:
                resp = new Response(false, "Group does not exist.");
                return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
            case ItemAlreadyRead:
                resp = new Response(false, "Read item already added.");
                return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        userGroupsRepository.save(userGroups);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *  Get feeds from group name in user groups
     *  @param request HTTP GET request, need Token in header Authorization
     *  @param body body of the request
     *  @return OK or BAD_REQUEST when request is invalid (unknown group or feed)
     *  @see Response for response structure
     * */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/items", method = RequestMethod.POST)
    public ResponseEntity<?> getReadItems(HttpServletRequest request, @RequestBody GetItemsBody body) {

        String username = validateRequest(request);
        String groupName = body.getGroupName();
        String feedName = body.getFeedName();

        if (username == null || groupName == null || feedName == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Getting user and its groups
        User user = userRepository.findByEmail(username);
        UserGroups userGroups = userGroupsRepository.findByUserId(user.getId());

        List<String> readItems = userGroupsBO.getReadItems(userGroups, groupName, feedName);

        if (readItems == null) {
            Response resp = new Response(false, "Group name or feed name does not exist.");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(readItems, HttpStatus.OK);
    }


}
