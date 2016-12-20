package com.minehut.gameplate.util;

/**
 * Created by luke on 12/19/16.
 */
public class Strings {

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

}
