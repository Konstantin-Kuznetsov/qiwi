package com.example.konstantin.qiwi.POJO;

import com.google.gson.annotations.SerializedName;

/**
 *  Валидатор поля
 *
 * Created by Konstantin on 08.12.2017.
 */

public class Validator {
    @SerializedName("type")
    private String type; // regex
    @SerializedName("predicate")
    private Predicate predicate;
    @SerializedName("message")
    private String message; // сообщение, если поле не валидно

    public String getType() {
        return type;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public String getMessage() {
        return message;
    }
}
