package com.example.konstantin.qiwi.POJO;

import com.google.gson.annotations.SerializedName;

/**
 *  Корневой элемент в JSON
 *
 * Created by Konstantin on 18.12.2017.
 */

public class ResponseJSON {
    @SerializedName("content")
    private Content content;

    public Content getContent() {
        return content;
    }
}
