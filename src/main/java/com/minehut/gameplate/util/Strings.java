package com.minehut.gameplate.util;

/**
 * Created by luke on 12/19/16.
 */
public class Strings {

    public static String getTechnicalName(String s) {
        return s.toUpperCase().replace(" ", "_");
    }

    /**
     * Simplifies a string by making it lowercase and removing spaces.
     *
     * @return The string in lowercase without spaces
     */
    public static String simplify(String string) {
        return string.toLowerCase().replace(" ", "");
    }

    /**
     * Checks checks if string1 starts with string2, used for user inputs
     *
     * @return if string1 simplified starts win string2 simplified
     */
    public static boolean matchString(String string1, String string2) {
        return simplify(string1).startsWith(simplify(string2));
    }

    public static String formatTime(double time) {
        boolean negative = false;
        if (time < 0) {
            negative = true;
            time *= -1;
        }
        int hours = (int) time / 3600;
        int minutes = (int) (time - (hours * 3600)) / 60;
        int seconds = (int) time - (hours * 3600) - (minutes * 60);
        String hoursString = hours + "";
        String minutesString = minutes + "";
        String secondsString = seconds + "";
        while (minutesString.length() < 2) {
            minutesString = "0" + minutesString;
        }
        while (secondsString.length() < 2) {
            secondsString = "0" + secondsString;
        }
        return (negative ? "-" : "") + (hours == 0 ? "" : hoursString + ":") + minutesString + ":" + secondsString;
    }

    public static int timeStringToSeconds(String input) {
        return (int) timeStringToExactSeconds(input);
    }

    public static double timeStringToExactSeconds(String input) {
        if (input.equals("oo"))
            return (int) Double.POSITIVE_INFINITY;
        if (input.equals("-oo"))
            return (int) Double.NEGATIVE_INFINITY;
        double time = 0;
        String currentUnit = "";
        String current = "";
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isDigit(c) && !currentUnit.equals("")) {
                time += convert(Numbers.parseDouble(current), currentUnit);
                current = "";
                currentUnit = "";
            }
            if (Character.isDigit(c) || c == '.') {
                current += c + "";
            } else if (c != '-') {
                currentUnit += c + "";
            }
        }
        time += convert(Numbers.parseDouble(current), currentUnit);
        if (input.startsWith("-")) time *= -1;
        return time;
    }

    private static double convert(double value, String unit) {
        switch (unit) {
            case "y":
                return value * 365 * 60 * 60 * 24;
            case "mo":
                return value * 31 * 60 * 60 * 24;
            case "d":
                return value * 60 * 60 * 24;
            case "h":
                return value * 60 * 60;
            case "m":
                return value * 60;
            case "s":
                return value;
        }
        return value;
    }

}
