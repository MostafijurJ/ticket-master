package com.learn.ms.event_service.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberFormatUtils {
    private static final DecimalFormat twoDecimalPlace = new DecimalFormat("#.00");
    public static BigDecimal twoDecimalPlace(BigDecimal number){
        return number.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
