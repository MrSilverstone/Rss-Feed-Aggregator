package com.epitech.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by gjura_r on 19/01/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteFeedBody {
    private String groupName;
    private String feedName;
}
