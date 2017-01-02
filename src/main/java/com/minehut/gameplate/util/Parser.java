package com.minehut.gameplate.util;

import org.bukkit.ChatColor;

/**
 * Created by luke on 12/24/16.
 */
public class Parser {

    public static ChatColor parseColor(String s) {
        s = s.toUpperCase().replace(" ", "_");
        return ChatColor.valueOf(s);
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
