package com.example.konstantin.qiwi.Validator;

import com.example.konstantin.qiwi.POJO.Validator;

import java.util.regex.Pattern;

/**
 * Принипает строку и валидирует в соответствии с regex, возвращая true/false
 *
 * Created by Konstantin on 12.12.2017.
 */

public class StringValidator {

    // объект-валидатор из json
    private Validator validator;

    public StringValidator(Validator validator) {
        this.validator = validator;
    }

    public Validator getValidatorInstance() {
        return validator;
    }

    // проверка переданной строки на соответствие паттерну
    public boolean isValid(String s) {
        return Pattern.matches(validator.getPredicate().getPattern(), s);
    }
}
