package com.mattg.viewmodelexample.utils;

import java.text.DecimalFormat;

public class Utils {

    private static DecimalFormat df = new DecimalFormat("#.##");

    public static Double defaultCurrencyStringCheck(String input) {
        if(input.isEmpty()){
            return 0.00;
        }
        return CurrencyUtil.currencyValue(input);
    }

    /**
     * Provides a properly formatted dollar String value of given double
     * @param amount
     * @return
     */
    public static String formatDoubleToCurrency(Double amount) {
        if(amount == 0.0){ return "$0.00";} else return "$" + df.format(amount);
    }
}
