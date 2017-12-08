package com.example.konstantin.qiwi.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 *  Содержит список elements[], т.е список View для отрисовки на форме
 *
 * Created by Konstantin on 08.12.2017.
 */

public class Content {

    @SerializedName("elements")
    private List<Element> elements = new ArrayList<Element>();

    public List<Element> getElements() {
        return elements;
    }
}
