package com.epitech.model;

/**
 * Created by loulo on 13/01/2017.
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ErrorMessage {

    public ErrorMessage(String message, String code) {
        this(message, code, "");
    }

    public ErrorMessage(String message) {
        this(message, "", "");
    }

    private String message;

    private String code;

    private String detail;

}
