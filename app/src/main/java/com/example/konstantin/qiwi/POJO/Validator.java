package com.example.konstantin.qiwi.POJO;

import com.google.gson.annotations.SerializedName;

/**
 *  Валидатор поля
 *
 * Created by Konstantin on 08.12.2017.
 */

public class Validator {
    @SerializedName("type")
    public String type; // regex
    @SerializedName("predicate")
    public Predicate predicate;
    @SerializedName("message")
    public String message; // сообщение, если поле не валидно
}
