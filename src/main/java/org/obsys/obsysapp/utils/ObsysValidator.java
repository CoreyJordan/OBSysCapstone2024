package org.obsys.obsysapp.utils;

public class ObsysValidator {
    protected boolean containsSymbol(String input) {
        for (Character c : input.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return true;
            }
        }
        return false;
    }

    protected boolean containsNumeric(String input) {
        for (Character c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    protected boolean containsLower(String input) {
        for (Character c : input.toCharArray()) {
            if (Character.isLowerCase(c)) {
                return true;
            }
        }
        return false;
    }

    protected boolean containsUpper(String input) {
        for (Character c : input.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }
}
