# qiwi test task
Задача
Отрисовать форму, поведение и вид которой определяется файлом в формате json (содержание файла находится в конце описания
задания и по урлу https://w.qiwi.com/mobile/form/form.json).
Форма должна реагировать на пользовательский ввод описанным в файле поведением.
Валидацию текстовых полей ввода можно проводить по кнопке "валидировать", но в идеале, использовать RxJava throttle метод, то есть
пользователь вводит, что хочет, и если ввода нет определенное количество времени – валидировать содержимое поля.
Можно использовать любой архитектурный паттерн, но для этой задачи неплохо подходит MVVM. Очень желательно разделять логику
построения дерева и рендеринг формы.

Предполагаемая идея решения
----------
- Идея решения - разместить создаваемые View в ленте RecyclerView, а при необходимости перестроить интерфейс - изменяем в адаптере этого списка набор данных и оповещаем его, `uiConstructorAdapter.notifyDataSetChanged()`

- При перестроении UI необходимо будет проходить по всем `Element` из первоначального списка из json и перепроверять видимость зависимых элементов. Например, при изменении типа авторизации с номера карты на номер телефона, нужно будет удалить из данных в адаптере все TextInputLayout, относящиеся к вводу номера карты(эти элементы являются потомками узла "тип авторизации") и добавить TextInputLayout, относящиеся к авторизации по номеру телефона. После этого можно оповестить адаптер об изменении набора данных.

- Адаптер, конструирующий по набору параметров элемент UI по типу элемента(текстовое поле или спиннер), использует соответствующую разметку xml. Тут будет выбор всего из двух типов.

Тут editTextView - это TextInputLayout 

```java
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
```
- Рассматривать каждый json как описание одного или нескольких деревьев, каждый узел которых является блоком параметров для передачи в конструктор ViewModel. Т.е на основе всех данных можно сконструировать дерево из ViewModel, потом каждый раз при изменении чего-либо обходить потомков измененного узла и изменять состояние VM(а за ними потянется  и UI) в соответствии с логикой.

В тестовом json максимальное число потомков – 2, но если абстрагироваться - нужно учитывать любое число.



RxJava, RxBinding
----------

С помощью RxBinding устанавливаем слушатель на поле ввода. И на спиннер тоже слушатель по изменению выбранного пункта.

https://academy.realm.io/posts/donn-felker-reactive-android-ui-programming-with-rxbinding/
http://reactivex.io/documentation/operators/debounce.html

должно получиться что-то похожее:

```java
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
```
