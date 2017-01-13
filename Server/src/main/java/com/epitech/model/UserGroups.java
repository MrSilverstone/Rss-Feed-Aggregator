package com.epitech.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "userGroups")
public class UserGroups {
    @Id
    private String id;
    @Indexed
    private String userId;
    private List<Group> groups;
}
