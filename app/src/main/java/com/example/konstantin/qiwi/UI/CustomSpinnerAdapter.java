package com.example.konstantin.qiwi.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.konstantin.qiwi.POJO.Choice;
import com.example.konstantin.qiwi.R;
import com.example.konstantin.qiwi.UI.ViewHolders.OnSpinnerItemClick;

import java.util.ArrayList;
import java.util.List;

/**
 *  Адаптер для спиннера, принимает List<Choice> для формирования списка
 *
 *  getView() и getDropDownView() реализованы отдельно, для установки слушателя нажатий
 *  только при выборе элемента в открытом списке.
 *
 * Created by Konstantin on 15.12.2017.
 */

public class CustomSpinnerAdapter extends ArrayAdapter<Choice> {

    private List<Choice> choicesList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private OnSpinnerItemClick onSpinnerItemClick; // интерфейс


    public CustomSpinnerAdapter(@NonNull Context context, int resource, List<Choice> choices) {
        super(context, resource, choices);
        choicesList = choices; // список для отображения в спиннере
        layoutInflater = LayoutInflater.from(context);
    }

    // вид элемента в закрытом списке, без clickListener
    @NonNull
    @Override
    public View getView(int position, @Nullable View closedSpinnerView, @NonNull ViewGroup parent) {
        // используем макет с одним только TextView, устанавливаем в поле текст из choicesList
        closedSpinnerView = layoutInflater.inflate(R.layout.spinner_dropdown_item, parent, false);
        TextView itemTextView = closedSpinnerView.findViewById(R.id.spinner_element_text);
        itemTextView.setText(choicesList.get(position).getTitle());
        return closedSpinnerView;
    }

    // вид при открытом списке,
    // все аналогично getView(), но есть отлеживание нажатий через clickListener.
    @Override
    public View getDropDownView(int position, @Nullable View openedSpinnerView, @NonNull ViewGroup parent) {
        openedSpinnerView = layoutInflater.inflate(R.layout.spinner_dropdown_item, parent, false);
        TextView itemTextView = openedSpinnerView.findViewById(R.id.spinner_element_text);
        itemTextView.setText(choicesList.get(position).getTitle());

        // установка слушателя
        itemTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSpinnerItemClick.onClick(position);
            }
        });

        return openedSpinnerView;
    }

    // установка интерфейса слушателя
    public void setClickListener(OnSpinnerItemClick listener) {
        onSpinnerItemClick = listener;
    }
}
