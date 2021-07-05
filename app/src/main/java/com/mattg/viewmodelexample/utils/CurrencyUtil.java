package com.mattg.viewmodelexample.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyUtil {

    /**
     * This will round string input to a proper double.  Should pass all currency amounts from
     * edit texts / user input through this one method, so that rounding is consistent througout the
     * app.  If it needs to be adjust to FLOOR or CEILING, it can then be done in one place (here)
     * @param input String to convert to currency formatted double
     * @return Double the value of input string
     */
    public static Double currencyValue(String input) {
        System.out.println("%%%%%%%%%%%%%%%%%%%%%% " + input);
        if(input != null) {
            BigDecimal format = new BigDecimal(input);
            return Double.parseDouble(String.valueOf(format.setScale(2, RoundingMode.HALF_EVEN)));
        } else
            return Double.parseDouble(String.valueOf(new BigDecimal("0.00").setScale(3, RoundingMode.HALF_EVEN)));
    }


 }
