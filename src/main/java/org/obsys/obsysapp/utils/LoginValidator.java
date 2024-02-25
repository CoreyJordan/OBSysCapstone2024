package org.obsys.obsysapp.utils;

public class LoginValidator extends ObsysValidator{
    private final String username;
    private final String password;

    public LoginValidator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean okToLogin() {
        return usernameIsValid() && passwordIsValid();
    }

    public boolean passwordIsValid() {
        return !password.isEmpty() &&
                // Check for whitespace prior to checking for symbol
                !password.contains(" ") &&
                containsUpper(password) &&
                containsLower(password) &&
                containsNumeric(password) &&
                containsSymbol(password) &&
                password.length() >= 6;
    }

    public boolean usernameIsValid() {
        return !username.isEmpty() &&
                !username.contains(" ") &&
                !containsSymbol(username);
    }
}
