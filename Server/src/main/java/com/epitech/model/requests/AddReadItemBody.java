package com.epitech.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddReadItemBody {
    private String groupName;
    private String feedName;
    private String identifier;
}