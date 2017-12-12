package com.example.konstantin.qiwi.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 *  Описание типа и внешнего вида одного конкретного View.
 *  Список полей выпадающего списка(для типа radio), либо text - простое поле ввода.
 *
 * Created by Konstantin on 08.12.2017.
 */

public class Widget {
    @SerializedName("type")
    private String type; // radio/text
    @SerializedName("choices")
    private List<Choice> choices = new ArrayList<Choice>(); // список значений выпадающего списка
    @SerializedName("keyboard")
    private String keyboard;

    public String getType() {
        return type;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public String getKeyboard() {
        return keyboard;
    }
}
