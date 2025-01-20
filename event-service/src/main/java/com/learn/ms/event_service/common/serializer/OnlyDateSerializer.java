package com.learn.ms.event_service.common.serializer;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.learn.ms.event_service.common.utils.DateTimeUtils;

import java.time.LocalDate;
import java.util.Date;


public class OnlyDateSerializer extends StdConverter<Object, String> {
    @Override
    public String convert(Object content) {
        if (content instanceof Date date) {
            return DateTimeUtils.dateToString(date);
        } else if (content instanceof LocalDate localDate) {
            return DateTimeUtils.localDateTime(localDate);
        } else {
            return null;
        }
    }
}
