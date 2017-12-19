package com.example.konstantin.qiwi.UIConstructor;

import android.os.Handler;
import android.text.InputType;

import com.example.konstantin.qiwi.POJO.Choice;
import com.example.konstantin.qiwi.POJO.Element;
import com.example.konstantin.qiwi.R;
import com.example.konstantin.qiwi.UI.CustomSpinnerAdapter;
import com.example.konstantin.qiwi.UI.ViewHolders.EditTextViewHolder;
import com.example.konstantin.qiwi.UI.ViewHolders.OnSpinnerItemClick;
import com.example.konstantin.qiwi.UI.ViewHolders.SpinnerViewHolder;
import com.example.konstantin.qiwi.Validator.StringValidator;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 *  Рендеринг элементов UI
 *
 * Created by Konstantin on 12.12.2017.
 */

public class UIConstructor {

    // хранилище всех подписок изменений View
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    // список имен элементов без повторов
    private Set<String> namesOfElements = new HashSet<>();

    // список ВСЕХ элементов(описание View) из переданного json
    private List<Element> allElements = new ArrayList<>();

    private static final String EMPTY_STRING = "";

    // список видимых в текущей конфигурации элементов
    private List<Element> visibleElements = new ArrayList<>();

    private UITreeIterator uiTreeIterator;

    public UIConstructor(List<Element> visibleElements, ItemsRecyclerAdapter adapter) {
        this.visibleElements = visibleElements;

        uiTreeIterator = new UITreeIterator(this.visibleElements, adapter);
    }

    // первоначальная инициализация элементов
    public void initiateItems(List<Element> elementInitialList, List<Element> visibleElements) {
        for (Element element : elementInitialList) {
            if (element.getType().equals("field")) { // тип - поле без зависимости
                visibleElements.add(element); // список видимых
            }
            allElements.add(element);
        }
    }

    public void initiateEditTextView(Element element, StringValidator stringValidator, EditTextViewHolder viewHolder) {

        // editTextView.getEditText() - ссылка на отслеживаемый EditText
        // сообщение о некорректном вводе формирует TextInputLayout
        // Вложенный элемент - TextInputEditText

        // Hint поля
        viewHolder.getTextInputLayout().setHint(element.getView().getPrompt());

        // текущий набранный текст
        viewHolder.getTextInputLayout().getEditText().setText(element.getView().getCurrText());

        // клавиатура
        if (element.getView().getWidget().getKeyboard() != null &&
                element.getView().getWidget().getKeyboard().equals("numeric")) {
            // цифровая клавиатура для ввода номера карты
            viewHolder.getTextInputLayout().getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            // обычная клавиатура
            viewHolder.getTextInputLayout().getEditText().setInputType(InputType.TYPE_CLASS_TEXT);
        }

        // Используя механизм RxBindings подписываемся на изменения текста в EditText
        Disposable editTextWatcher = RxTextView.textChanges(viewHolder.getTextInputLayout().getEditText())
                .debounce(500, TimeUnit.MILLISECONDS) // задержка 500мс перед обработкой
                .map(CharSequence::toString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(currText -> {
                    // проверка на пустое поле - чтобы ошибка не светилась сразу при показе формы
                    // если текст есть и не проходит валидацию - устанавливаем ошибку
                    if (!currText.equals(EMPTY_STRING) &&!stringValidator.isValid(currText)) {
                        viewHolder.getTextInputLayout().setError(stringValidator
                                .getValidatorInstance()
                                .getMessage()); // установка ошибки текстовому полю
                    } else viewHolder.getTextInputLayout().setError(EMPTY_STRING); // сброс ошибки
                });

        // добавление в пул подписок
        compositeDisposable.add(editTextWatcher);
    }


    // в метод передается Element с описанием спиннера для конструирования
    public void initiateSpinnerView(SpinnerViewHolder viewHolder, Element element) {

        // список элементов спиннера, передается ArrayAdapter спиннера
        List<Choice> choiceList = element.getView().getWidget().getChoices();

        // установка кастомизированного адаптера для спиннера (контекст, xml_разметка, список_элементов)
        CustomSpinnerAdapter spinnerAdapter  =  new CustomSpinnerAdapter(viewHolder.itemView.getContext(), R.layout.spinner_dropdown_item, choiceList);
        viewHolder.getSpinnerView().setAdapter(spinnerAdapter);

        // Устанавливаем на адаптер слушатель. Выбираем пункт меню по умолчанию 1
        Disposable spinnerClickDisposable = Observable.create(e -> {
            OnSpinnerItemClick listener = e::onNext;
            spinnerAdapter.setClickListener(listener);
        })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    uiTreeIterator.insertChilds(choiceList.get((Integer) integer).getValue(),
                            element.getName(),
                            allElements);
                    new Handler().post(() -> viewHolder.getSpinnerView().setSelection((Integer) integer));
                });

        // добавление в пул подписок
        compositeDisposable.add(spinnerClickDisposable);


        // Используя механизм RxBindings подписываемся на выбор элемента спиннера
        Disposable elementSelectedListener = RxAdapterView.itemSelections(viewHolder.getSpinnerView())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    if (!namesOfElements.contains(element.getName())) {
                        // построение(перестроение) дерева элементов
                        // вставка в список элементов, которые будут показаны при выбранном значении
                        // спиннера, т.е потомков текущего узла, условие видимости которых удовлетворено
                        uiTreeIterator.insertChilds(choiceList.get(integer).getValue(), // int идентификатор выбранного
                                element.getName(), // поле name этого спиннера для построения дерева потомков
                                allElements); // List<Element>
                        namesOfElements.add(element.getName());
                    }
                });

        // добавление в пул подписок
        compositeDisposable.add(elementSelectedListener);
    }

}
