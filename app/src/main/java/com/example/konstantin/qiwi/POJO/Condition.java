package com.example.konstantin.qiwi.POJO;

import com.google.gson.annotations.SerializedName;

/**
 *  Condition определяет видимость объекта content
 *
 * Created by Konstantin on 08.12.2017.
 */

public class Condition {

    @SerializedName("type")
    private String type;
    @SerializedName("field")
    private String field; // от какого поля зависит View, которому принадлежит данный Condition
    @SerializedName("predicate")
    private Predicate predicate; // содержит regex, применяемый к "field". возвращает true/false

    public String getType() {
        return type;
    }

    public String getField() {
        return field;
    }

    public Predicate getPredicate() {
        return predicate;
    }
}
