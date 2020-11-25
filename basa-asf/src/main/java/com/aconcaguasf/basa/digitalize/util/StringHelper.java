package com.aconcaguasf.basa.digitalize.util;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class StringHelper {
    private static StringHelper instance;

    public static StringHelper getInstance() {
        return (instance != null) ? instance : new StringHelper();
    }

    public List<String> stringToListString(String listString) {
        return Arrays.asList(listString.split("\\s*,\\s*"));
    }

    public List<Long> stringToListLong(String listString) {
        return stringToListString(listString).stream().map(Long::parseLong).collect(Collectors.toList());
    }

    public BigDecimal truncateDecimal(double x, int numberofDecimals) {
        if (x > 0) {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_FLOOR);
        } else {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_CEILING);
        }
    }

    public String convertScoresToComma(String text) {
        return StringUtils.replace(text, "-", ".");
    }

    public String dateToString(Date pFecha, String pMask){
        DateFormat formatoFecha;

        switch (pMask){
            case "dd/mm/yyyy":
                formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                break;
            case "yyyy-mm-dd":
                formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                break;
            default:
                formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                break;
        }
        return formatoFecha.format(pFecha);
    }

    public Calendar stringToCalendar(String pFecha, String pMask) throws ParseException {
        DateFormat formatoFecha;
        Date fechaDate;
        Calendar fechaCalendar = Calendar.getInstance();
        switch (pMask){
            case "dd/mm/yyyy":
                formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                break;
            default:
                formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                break;
        }
        fechaCalendar.setTime(formatoFecha.parse(pFecha));
        return fechaCalendar;
    }

    public String capitalize(String text) {
        if (text.length() == 0)
            return text;
        char[] array = text.toLowerCase().toCharArray();
        array[0] = Character.toUpperCase(array[0]);
        for (int i = 1; i < array.length; i++) {
            if (Character.isWhitespace(array[i - 1]))
                array[i] = Character.toUpperCase(array[i]);
        }
        return new String(array);
    }


}
