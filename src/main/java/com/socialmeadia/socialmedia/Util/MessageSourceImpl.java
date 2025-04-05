package com.socialmeadia.socialmedia.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageSourceImpl {

    @Autowired
    private MessageSource messageSource;

    public String getMessage(String msgKey) {
        return messageSource.getMessage(msgKey, null, LocaleContextHolder.getLocale());
    }
}
