package com.epitech.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "userGroups")
public class UserGroups {
    @Id
    private String id;
    @Indexed
    private String userId;
    private List<Group> groups;

    public UserGroups() {

    }

    public UserGroups(String id, String userId, List<Group> groups) {
        this.id = id;
        this.userId = userId;
        this.groups = groups;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
