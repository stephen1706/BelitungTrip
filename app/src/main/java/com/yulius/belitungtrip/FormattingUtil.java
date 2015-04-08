package com.yulius.belitungtrip;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FormattingUtil {
    public static String formatDecimal(int number){
        DecimalFormatSymbols decimalSymbol = new DecimalFormatSymbols(new Locale("in"));
        DecimalFormat decimalFormat = new DecimalFormat("#,###", decimalSymbol);
        String formattedDecimal = decimalFormat.format(number);
        return formattedDecimal;
    }
}
