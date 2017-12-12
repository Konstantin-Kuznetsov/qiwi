package com.example.konstantin.qiwi.UIConstructor;

import android.database.DataSetObserver;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.konstantin.qiwi.POJO.Choice;
import com.example.konstantin.qiwi.POJO.Element;
import com.example.konstantin.qiwi.Validator.StringValidator;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Konstantin on 12.12.2017.
 */

public class UIConstructor {
    // список ВСЕХ элементов(описание View) из переданного json
    private List<Element> allElements = new ArrayList<>();

    // список видимых в текущей конфигурации элементов
    private List<Element> visibleElements = new ArrayList<>();

    // Map<regex, validator>
    private Map<String, StringValidator> validatorsCache = new HashMap<>();

    private Set<String> namesOfElements = new HashSet<>();

    // хранилище всех подписок изменений View
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final String EMPTY_STRING = "";

    public void initiateItems(List<Element> elementInitialList) {
        for (Element element : elementInitialList) {
            if (element.getType().equals("field")) { // тип - поле без зависимости
                visibleElements.add(element); // список видимых
            }
            allElements.add(element);
        }
        // адаптер notifyDataSetChanged();
    }


    // в метод передается Element с описанием спиннера для конструирования
    private void initiateSpinnerView(Element element, Spinner spinner) {
        // список элементов спиннера, передается адаптеру спиннера
        List<Choice> choiceList = element.getView().getWidget().getChoices();

        // SpinnerAdapter spinnerAdapter = TODO: доделать конфигуратор адаптера
        // spinner.setAdapter(spinnerAdapter);

    }

    // в метод передается Element с описанием текстового поля для конфигурирования
    private void initiateEditTextView(Element element, StringValidator stringValidator, TextInputLayout editTextView) {

        // editTextView.getEditText() - ссылка на отслеживаемый EditText
        // сообщение о некорректном вводе формирует TextInputLayout

        // Hint поля
        editTextView.getEditText().setHint(element.getView().getPrompt());

        // текущий набранный текст
        editTextView.getEditText().setText(element.getView().getCurrText());

        // клавиатура
        if (element.getView().getWidget().getKeyboard() != null &&
                element.getView().getWidget().getKeyboard().equals("numeric")) {
            // цифровая клавиатура для ввода номера карты
            editTextView.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            // обычная клавиатура
            editTextView.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);
        }

        Disposable editTextWatcher = RxTextView.textChanges(editTextView.getEditText())
                .debounce(500, TimeUnit.MILLISECONDS) // задержка 500мс перед обработкой
                .map(CharSequence::toString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(currText -> {
                    if (stringValidator.isValid(currText)) {
                        editTextView.setError(stringValidator
                                .getValidatorInstance()
                                .getMessage()); // установка ошибки текстовому полю
                    } else editTextView.setError(EMPTY_STRING); // сброс ошибки
                });

        // добавление в пул подписок
        compositeDisposable.add(editTextWatcher);
    }


}
