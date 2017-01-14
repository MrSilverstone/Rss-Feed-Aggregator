package com.epitech.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadItemRequest {
    private String url;
    private String groupName;
    private String identifier;
}
