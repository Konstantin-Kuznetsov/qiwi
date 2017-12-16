package com.example.konstantin.qiwi.UIConstructor;

import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;

import com.example.konstantin.qiwi.POJO.Choice;
import com.example.konstantin.qiwi.POJO.Element;
import com.example.konstantin.qiwi.POJO.Validator;
import com.example.konstantin.qiwi.R;
import com.example.konstantin.qiwi.UI.CustomSpinnerAdapter;
import com.example.konstantin.qiwi.UI.ViewHolders.OnSpinnerItemClick;
import com.example.konstantin.qiwi.UI.ViewHolders.SpinnerViewHolder;
import com.example.konstantin.qiwi.Validator.StringValidator;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
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
    private Map<String, StringValidator> validatorsMap = new HashMap<>();

    // список имен элементов без повторов
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
    private void initiateSpinnerView(SpinnerViewHolder viewHolder, Element element) {

        // список элементов спиннера, передается ArrayAdapter спиннера
        List<Choice> choiceList = element.getView().getWidget().getChoices();

        // установка кастомизированного адаптера для спиннера (контекст, xml_разметка, список_элементов)
        CustomSpinnerAdapter spinnerAdapter  =  new CustomSpinnerAdapter(viewHolder.itemView.getContext(), R.layout.spinner_dropdown_item, choiceList);
        viewHolder.getSpinnerView().setAdapter(spinnerAdapter);

        // Устанавливаем на адаптер слушатель. Выбираем пункт меню по умолчанию
        Disposable spinnerClickDisposable = Observable.create(e -> {
            OnSpinnerItemClick listener = e::onNext;
            spinnerAdapter.setClickListener(listener);
        })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    insertChilds(choiceList.get((Integer) integer).getValue(),
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
                        insertChilds(choiceList.get(integer).getValue(), // int идентификатор выбранного
                                element.getName(), // поле name этого спиннера для построения дерева потомков
                                allElements); // List<Element>
                        namesOfElements.add(element.getName());
                    }
                });

        // добавление в пул подписок
        compositeDisposable.add(elementSelectedListener);
    }

    private void initiateEditTextView(Element element, StringValidator stringValidator, TextInputLayout editTextView) {

        // editTextView.getEditText() - ссылка на отслеживаемый EditText
        // сообщение о некорректном вводе формирует TextInputLayout
        // Вложенный элемент - TextInputEditText

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

        // Используя механизм RxBindings подписываемся на изменения текста в EditText
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

    // Удаление детей элемента, строковый идентификатор которого мы передаем в метод.
    // Использование Iterator, а не foreach обусловлено необходимостью изменять перебираемый массив.
    // В методе перебираются все элементы массива отображаемых элементов(visibleElements).
    // Если находим элемент, родителем которого является переданный в метод - удаляем.
    // Рекурсивно обходим все.
    private void deleteChild(String parentFiled) {
        Iterator<Element> elementIterator = visibleElements.iterator();
        while (elementIterator.hasNext()) {
            Element element = elementIterator.next();
            if (element.getParent() != null && element.getParent().equals(parentFiled)) {
                if (element.getContent() != null && element.getContent().getElements() != null) {
                    deleteChild(parentFiled);
                }
                elementIterator.remove();
            }
        }
    }

    // Вставка в дерево зависимостей элементов нового, вставка в массив показываемых элементов(visibleElements)
    // если удовлетворено условие видимости- проверяется валидатором.
    private void insertChilds(String toValidate, String parent, List<Element> allElementsList) {
        deleteChild(parent); // удаление потомков узла "parent"
        for (int i =0; i < allElementsList.size(); i++) {
            Element element = allElementsList.get(i);
            // если нашли элемент, у которого родительским прописан переданный в метод, проверяем
            // выполнение видимости, через валидатор
            if (element.getCondition() != null && element.getCondition().getField().equals(parent) &&
                    element.getCondition().getPredicate() != null) {
                // в конструктор передается соответствующий элементу Validator
                StringValidator validator = new StringValidator(new Validator(element.getCondition().getPredicate()));
                if (validator.isValid(toValidate)) {
                    for (int j = 0; j < element.getContent().getElements().size(); j++) {
                        // установка родителя вложенным элементам
                        element.getContent().getElements().get(j).setParent(parent);
                        // проверка - новый ли элемент добавляем в список видимых, сброс текста в поле
                        if (!visibleElements.contains(element.getContent().getElements().get(j))) {
                            element.getContent().getElements().get(j).getView().setCurrText(""); // текст в элементе
                            visibleElements.add(element.getContent().getElements().get(j));
                        }
                    }
                }
            }
        }
        // обновление набора данныз адаптера RecyclerView для отображения добавленных элементов
        notifyDataSetChanged(); // TODO: перенести часть методов в ItemsRecyclerAdapter
    }

}
