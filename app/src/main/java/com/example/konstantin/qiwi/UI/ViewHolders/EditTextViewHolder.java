package com.example.konstantin.qiwi.UI.ViewHolders;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Spinner;

import com.example.konstantin.qiwi.R;

/**
 *  ViewHolder для любого элемента, содержащего TextInputEditText внутри TextInputLayout
 *
 * Created by Konstantin on 15.12.2017.
 */

public class EditTextViewHolder extends RecyclerView.ViewHolder{

    private TextInputEditText textInputEditText;
    private TextInputLayout textInputLayout;

    public EditTextViewHolder(View itemView) {
        super(itemView);
        textInputLayout = (TextInputLayout) itemView.findViewById(R.id.text_input_layout);
        textInputEditText = itemView.findViewById(R.id.edit_text_field);
    }

    public TextInputEditText getTextInputEditText() {
        return textInputEditText;
    }

    public TextInputLayout getTextInputLayout() {
        return textInputLayout;
    }
}
