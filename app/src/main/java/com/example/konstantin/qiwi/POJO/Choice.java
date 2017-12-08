package com.example.konstantin.qiwi.POJO;

import com.google.gson.annotations.SerializedName;

/**
 *  Список значений выпадающего списка
 *
 * Created by Konstantin on 08.12.2017.
 */

public class Choice {
    @SerializedName("value")
    public String value; // константа для каждой строки списка
    @SerializedName("title")
    public String title; // текстовое описание
}
