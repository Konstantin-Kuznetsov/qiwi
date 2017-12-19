package com.example.konstantin.qiwi.UIConstructor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.konstantin.qiwi.POJO.Element;
import com.example.konstantin.qiwi.R;
import com.example.konstantin.qiwi.UI.ViewHolders.EditTextViewHolder;
import com.example.konstantin.qiwi.UI.ViewHolders.SpinnerViewHolder;
import com.example.konstantin.qiwi.Validator.StringValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  адаптер RecyclerView, содержащего сгенерированные View
 *
 * Created by Konstantin on 12.12.2017.
 */

public class ItemsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "qiwi_test_task";

    // константы для getItemViewType
    private static final int SPINNER_ITEM = 1;
    private static final int EDITTEXT_ITEM = 2;

    // константы из Widget.getType() указывающие на тип элемента
    private static final String SPINNER_TYPE = "radio";
    private static final String EDITTEXT_TYPE = "text";

    // список видимых в текущей конфигурации элементов
    private List<Element> visibleElements = new ArrayList<>();

    // Map<regex, validator>
    private Map<String, StringValidator> validatorsMap = new HashMap<>();

    // UIConstructor рендерит UI
    private UIConstructor uiConstructor = new UIConstructor(visibleElements, this);


    // первоначальная инициализация элементов
    public void initiateItems(List<Element> elementInitialList) {
        uiConstructor.initiateItems(elementInitialList, visibleElements);

        // новые данные адаптеру
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SPINNER_ITEM) {
            return new SpinnerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_element, parent, false));
        } else {
            return new EditTextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.edittext_element, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {

            Element element = visibleElements.get(holder.getAdapterPosition());

            if (element.getValidator() != null) {
                // если валидатора с заданным regex паттерном нет - создаем
                StringValidator validator = validatorsMap.get(element.getName() + element.getValidator().getMessage());
                if (validator == null) {
                    validator = new StringValidator(element.getValidator());
                    // добавляем вновь созданный
                    validatorsMap.put(element.getName() + element.getValidator().getMessage() ,validator);
                }

                if (holder.getItemViewType() == EDITTEXT_ITEM) {
                    EditTextViewHolder textViewHolder = (EditTextViewHolder) holder;
                    uiConstructor.initiateEditTextView(element, validator, textViewHolder);
                }
                if (holder.getItemViewType() == SPINNER_ITEM) {
                    SpinnerViewHolder spinnerItemViewHolder = (SpinnerViewHolder) holder;
                    uiConstructor.initiateSpinnerView(spinnerItemViewHolder, element);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return visibleElements.size();
    }

    // определение типа элемента для построения View
    @Override
    public int getItemViewType(int position) {
        if (visibleElements.get(position).getView().getWidget().getType().equals(SPINNER_TYPE)) {
            return SPINNER_ITEM;
        } else {
            return EDITTEXT_ITEM;
        }
    }
}
