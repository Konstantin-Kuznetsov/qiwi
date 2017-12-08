package com.example.konstantin.qiwi.POJO;

import com.google.gson.annotations.SerializedName;

/**
 *  Список значений выпадающего списка
 *
 * Created by Konstantin on 08.12.2017.
 */

public class Choice {
    @SerializedName("value")
    private String value; // константа для каждой строки списка
    @SerializedName("title")
    private String title; // текстовое описание

    public String getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }
}
