package com.example.konstantin.qiwi.POJO;

import com.google.gson.annotations.SerializedName;

/**
 *  Условие проверки видимости для View, паттерн проверки. Возвращает true/false
 *
 * Created by Konstantin on 08.12.2017.
 */

public class Predicate {
    @SerializedName("type")
    private String type; // везде regex
    @SerializedName("pattern")
    private String pattern; // regex паттерн

    public String getType() {
        return type;
    }

    public String getPattern() {
        return pattern;
    }
}
