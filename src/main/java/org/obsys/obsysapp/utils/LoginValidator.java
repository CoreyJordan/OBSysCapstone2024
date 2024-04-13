package org.obsys.obsysapp.utils;

public class LoginValidator extends ObsysValidator{
    private final String username;
    private final String password;

    public LoginValidator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Verifies username and password input validity.
     * @return True if both username and password are syntactically correct.
     */
    public boolean okToLogin() {
        return usernameIsValid() && passwordIsValid();
    }

    /**
     * Checks the username against the business rules for valid usernames. This
     * does NOT indicate the database contains the given username.
     * @return True if username passes all checks.
     */
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

    /**
     * Checks the password against the business rules for valid passwords. This
     * does NOT indicate the database contains the given password.
     * @return True if password passes all checks.
     */
    public boolean usernameIsValid() {
        return !username.isEmpty() &&
                !username.contains(" ") &&
                !containsSymbol(username);
    }
}
