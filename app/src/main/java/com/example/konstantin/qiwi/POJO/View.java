package com.example.konstantin.qiwi.POJO;

import com.google.gson.annotations.SerializedName;

/**
 *  Определяет тип и вид View (текстовое поле, выпадающий список...)
 *  hint text, название поля и т.д
 *
 * Created by Konstantin on 08.12.2017.
 */

public class View {
    @SerializedName("title")
    private String title; // название поля
    @SerializedName("prompt")
    private String prompt; // hint text
    @SerializedName("widget")
    private Widget widget; // описание непосредственно View

    public String getTitle() {
        return title;
    }

    public String getPrompt() {
        return prompt;
    }

    public Widget getWidget() {
        return widget;
    }
}
