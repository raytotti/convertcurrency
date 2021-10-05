package com.raytotti.convertcurrency.commons.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Objects;

@RestControllerAdvice
public class EnumRequestErrorHandler {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public FieldRequestError handle(HttpMessageNotReadableException exception) {

        String genericMessage = "Unacceptable JSON " + exception.getMessage();
        FieldRequestError errorResponse = new FieldRequestError("BAD_REQUEST", genericMessage);

        if (exception.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ifx = (InvalidFormatException) exception.getCause();
            if (ifx.getTargetType() != null && ifx.getTargetType().isEnum()) {
                Object[] args = Lists.newArrayList(ifx.getValue(), Arrays.toString(ifx.getTargetType().getEnumConstants())).toArray();
                String message = messageSource.getMessage("Transaction.currency.invalid", args, LocaleContextHolder.getLocale());
                String field = ifx.getPath().get(ifx.getPath().size() - 1).getFieldName();
                errorResponse = new FieldRequestError(field, message);
            }
        }

        return errorResponse;
    }

}
