package com.example.konstantin.qiwi.POJO;

import com.google.gson.annotations.SerializedName;

/**
 *  Описание типа, текста, поведения, видимости одного конкретного View для размещения на форме.
 *
 * Created by Konstantin on 08.12.2017.
 */

public class Element {

    @SerializedName("type")
    private String type;
    @SerializedName("name")
    private String name;
    @SerializedName("value")
    private String value;
    @SerializedName("validator")
    private Validator validator;
    @SerializedName("view")
    private View view;
    @SerializedName("condition")
    private Condition condition;

    private Content content;

    // имя родительского элемента в дереве элементов UI.
    // используется при рекурсивном обходе дерева для идентификации
    private String parent;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Validator getValidator() {
        return validator;
    }

    public View getView() {
        return view;
    }

    public Condition getCondition() {
        return condition;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Content getContent() {
        return content;
    }
}
