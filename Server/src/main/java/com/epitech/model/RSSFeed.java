package com.epitech.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RSSFeed {
    private String title;
    private String link;
    private String description;
    private String language;
    private String copyright;
    private String pubDate;
    private List<FeedMessage> messages;
}
