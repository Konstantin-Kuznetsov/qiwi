package com.example.konstantin.qiwi.UIConstructor;

import com.example.konstantin.qiwi.POJO.Element;
import com.example.konstantin.qiwi.POJO.Validator;
import com.example.konstantin.qiwi.Validator.StringValidator;

import java.util.Iterator;
import java.util.List;

/**
 *  Обход дерева зависимостей элементов UI
 *  Перестроение UI
 *  Удаление/добавление элементов в дерево отображаемых на форме
 *
 * Created by Konstantin on 19.12.2017.
 */

public class UITreeIterator {

    private List<Element> visibleElements;

    private ItemsRecyclerAdapter itemsRecyclerAdapter;

    public UITreeIterator(List<Element> visibleElements, ItemsRecyclerAdapter itemsRecyclerAdapter) {
        this.visibleElements = visibleElements;
        this.itemsRecyclerAdapter = itemsRecyclerAdapter;
    }

    // Удаление детей элемента, строковый идентификатор которого мы передаем в метод.
    // Использование Iterator, а не foreach обусловлено необходимостью изменять перебираемый массив.
    // В методе перебираются все элементы массива отображаемых элементов(visibleElements).
    // Если находим элемент, родителем которого является переданный в метод - удаляем.
    // Рекурсивно обходим все.
    public void deleteChild(String parentFiled) {
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
    public void insertChilds(String toValidate, String parent, List<Element> allElements) {
        deleteChild(parent); // удаление потомков узла "parent"
        for (int i =0; i < allElements.size(); i++) {
            Element element = allElements.get(i);
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
        itemsRecyclerAdapter.notifyDataSetChanged();
    }


}
