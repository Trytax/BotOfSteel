package com.omega;

public class StringUtils {

    public static boolean isDecimal(String value) {
        boolean pointFound = false;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (!Character.isDigit(c)) {
                if (c == '.' || c == ',') {
                    pointFound = true;
                } else {
                    return false;
                }
            }
        }

        return pointFound;
    }

    public static boolean isInteger(String value) {
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isBoolean(String value) {
        if (value.equals("1") || value.equals("0") || value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return true;
        }

        return false;
    }

    public static Object parse(String value) {
        if (StringUtils.isDecimal(value)) {
//            try {
//                return Float.parseFloat(value);
//            } catch (NumberFormatException e) {
//                return Double.parseDouble(value);
//            }
            return Double.parseDouble(value);
        } else if (StringUtils.isInteger(value)) {
//            try {
//                return Integer.parseInt(value);
//            } catch (NumberFormatException e) {
//                return Long.parseLong(value);
//            }
            return Long.parseLong(value);
        } else if (StringUtils.isBoolean(value)) {
            if (value.equals("1") || value.equalsIgnoreCase("true")) {
                return true;
            } else {
                return false;
            }
        } else {
            return value;
        }
    }
}
