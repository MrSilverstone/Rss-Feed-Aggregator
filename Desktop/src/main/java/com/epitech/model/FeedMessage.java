package com.epitech.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedMessage {
    private String title;
    private String description;
    private String link;
    private String author;
    private String guid;
}
