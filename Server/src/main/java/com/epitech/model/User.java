package com.epitech.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection="users")
public class User {
    @Id
    private String id;
    private String email;
    private String password;
    private String authorities;

    public User(String email, String password, String authorities){
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }
}