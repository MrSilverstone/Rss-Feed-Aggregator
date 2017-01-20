package com.epitech.business;

import com.epitech.model.Feed;
import com.epitech.model.Group;
import com.epitech.model.UserGroups;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 *  UserGroups business object
 * */
public class UserGroupsBO {

    public enum ErrorType {
        NoError,
        GroupDoesNotExist,
        FeedExists,
        FeedDoesNotExist,
        ItemAlreadyRead,
    }

    private ErrorType error;

    /**
     *  Add group to user groups
     *  @param userGroups userGroups in which insert elem
     *  @param group group to add
     *  @return true if addition succeeded, false otherwise (group already exists)
     * */
    public boolean addGroup(UserGroups userGroups, Group group) {
        List<Group> groups = userGroups.getGroups();

        Stream<Group> streamGroup = groups.stream();
        Stream<Group> existingGroupsForGroupName = streamGroup
                .filter(elem -> elem.getName().equals(group.getName()));

        if (existingGroupsForGroupName.count() > 0) {
            return false;
        }

        groups.add(group);
        userGroups.setGroups(groups);

        return true;
    }

    /**
     *  Remove group from user groups
     *  @param userGroups userGroups in which insert elem
     *  @param groupName name of the group to delete
     *  @return true if addition succeeded, false otherwise (group doest not exist)
     * */
    public boolean deleteGroup(UserGroups userGroups, String groupName) {
        List<Group> groups = userGroups.getGroups();
        Stream<Group> groupsStream = groups.stream();

        Stream<Group> resultStream = groupsStream.filter(group -> !group.getName().equals(groupName));
        List<Group> resultGroups = resultStream.collect(Collectors.toList());

        if (resultGroups.size() == groups.size()) {
            return false;
        }

        userGroups.setGroups(resultGroups);

        return true;
    }

    /**
     *  Add feed to group in user groups
     *  @param userGroups userGroups in which insert elem
     *  @param feed feed to add
     *  @param inGroup group in which to add feed
     *
     *  @return NoError: if succeeded, otherwise
     *  GroupDoesNotExist: when missing group or
     *  FeedExists: when feed already added
     * */
    public ErrorType addFeedToGroup(UserGroups userGroups, Feed feed, String inGroup) {
        List<Group> groups = userGroups.getGroups();
        Stream<Group> groupStream = groups.stream();

        error = ErrorType.GroupDoesNotExist;

        // Mapping groups
        List<Group> resultGroups = groupStream
                .map(group -> {
                    // Group name found in groups
                    if (group.getName().equals(inGroup)) {
                        List<Feed> feeds = group.getFeeds();
                        Stream<Feed> feedStream = feeds.stream();

                        // Checking if feed already added
                        List<Feed> resultStream = feedStream.
                                filter(elem -> elem.getUrl().equals(feed.getUrl()))
                                .collect(Collectors.toList());

                        if (resultStream.isEmpty()) {
                            feeds.add(feed);
                            group.setFeeds(feeds);

                            error = ErrorType.NoError;
                        } else {
                            error = ErrorType.FeedExists;
                        }
                    }

                    return group;
                })
                .collect(Collectors.toList());

        userGroups.setGroups(resultGroups);

        return error;
    }

    /**
     *  Add read item to feed in group in user groups
     *  @param userGroups userGroups in which insert elem
     *  @param feedName feed in which to insert read item
     *  @param identifier identifier of read item
     *  @param groupName of the feed's group
     *
     *  @return NoError: if succeeded, otherwise
     *  GroupDoesNotExist: when missing group or
     *  FeedDoesNotExist: when feed already added or
     *  ItemAlreadyRead: when item is already read
     * */
    public ErrorType addReadItemInFeed(UserGroups userGroups,
                                     String feedName,
                                     String identifier,
                                     String groupName) {

        error = ErrorType.GroupDoesNotExist;

        List<Group> groups = userGroups.getGroups();
        List<Group> updatedGroups = groups
                .stream()
                .map( group -> {
                    if (group.getName().equals(groupName)) {
                        List<Feed> checkIfExists = group.getFeeds()
                                .stream()
                                .filter(elem -> elem.getUrl().equals(feedName))
                                .collect(Collectors.toList());

                        if (!checkIfExists.isEmpty()) {
                            List<Feed> updatedFeeds = group.getFeeds()
                                    .stream()
                                    .map(feed -> {
                                        if (feed.getUrl().equals(feedName)) {
                                            List<String> readItems = feed.getReadItems();

                                            if (!readItems.contains(identifier)) {
                                                readItems.add(identifier);
                                                feed.setReadItems(readItems);

                                                error = ErrorType.NoError;
                                            } else {
                                                error = ErrorType.ItemAlreadyRead;
                                            }
                                        }

                                       return feed;
                                    })
                                    .collect(Collectors.toList());
                            group.setFeeds(updatedFeeds);
                        } else {
                            error = ErrorType.FeedDoesNotExist;
                        }
                    }
            return group;
        })
                .collect(Collectors.toList());

        userGroups.setGroups(updatedGroups);

        return error;
    }

    /**
     *  Get the feeds from group name in user groups
     *  @param userGroups userGroups in which insert elem
     *  @param groupName group name of the feed's group
     *  @return list of feeds, otherwise null
     * */
    public List<Feed> getFeeds(UserGroups userGroups, String groupName) {
        for (Group group : userGroups.getGroups()) {
            if (group.getName().equals(groupName)) {
                return group.getFeeds();
            }
        }

        return null;
    }


    public ErrorType deleteFeed(UserGroups userGroups, String groupName, String feedName) {
        List<Group> groups = userGroups.getGroups();
        Stream<Group> groupStream = groups.stream();

        error = ErrorType.GroupDoesNotExist;

        // Mapping groups
        List<Group> resultGroups = groupStream
                .map(group -> {
                    // Group name found in groups
                    if (group.getName().equals(groupName)) {
                        List<Feed> feeds = group.getFeeds();
                        Stream<Feed> feedStream = feeds.stream();

                        List<Feed> resultFeeds = feedStream.
                                filter(elem -> !elem.getUrl().equals(feedName))
                                .collect(Collectors.toList());

                        if (feeds.size() != resultFeeds.size()) {
                            group.setFeeds(resultFeeds);
                            error = ErrorType.NoError;
                        } else {
                            error = ErrorType.FeedDoesNotExist;
                        }
                    }

                    return group;
                })
                .collect(Collectors.toList());

        userGroups.setGroups(resultGroups);

        return error;
    }

    /**
     *  Get read items from feed in user groups
     *  @param userGroups userGroups in which insert elem
     *  @param groupName group name of the feed's group
     *  @param feedName name of the feed
     *  @return list read items, otherwise null
     * */
    public List<String> getReadItems(UserGroups userGroups, String groupName, String feedName) {
        for (Group group : userGroups.getGroups()) {
            if (group.getName().equals(groupName)) {
                for (Feed feed : group.getFeeds()) {
                    if (feed.getUrl().equals(feedName)) {
                        return feed.getReadItems();
                    }
                }
            }
        }
        return null;
    }

    public ErrorType deleteReadItems(UserGroups userGroups, String groupName, String feedName) {
        error = ErrorType.GroupDoesNotExist;

        Stream<Group> groups = userGroups
                .getGroups()
                .stream()
                .map( group -> {
                    if (group.getName().equals(groupName)) {
                        List<Feed> checkIfExists = group.getFeeds()
                                .stream()
                                .filter(elem -> elem.getUrl().equals(feedName))
                                .collect(Collectors.toList());

                        if (!checkIfExists.isEmpty()) {
                            Stream<Feed> feeds = group.getFeeds().stream().map( feed -> {
                                if (feed.getUrl().equals(feedName)) {
                                    feed.setReadItems(new ArrayList<>());
                                    error = ErrorType.NoError;
                                }

                                return feed;
                            });

                            group.setFeeds(feeds.collect(Collectors.toList()));
                        } else {
                            error = ErrorType.FeedDoesNotExist;
                        }
                    }
                    return group;
                });

        List<Group> updatedGroups = groups.collect(Collectors.toList());
        userGroups.setGroups(updatedGroups);

        return error;
    }
}
