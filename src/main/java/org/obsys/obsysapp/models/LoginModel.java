package org.obsys.obsysapp.models;

import javafx.beans.property.*;

public class LoginModel {
    private StringProperty username = new SimpleStringProperty("");
    private StringProperty password = new SimpleStringProperty("");

    public void setPassword(String password) {
        this.password.set(password);
    }

    private final StringProperty invalidLogin = new SimpleStringProperty("");
    private final StringProperty invalidPassword = new SimpleStringProperty("");
    private final StringProperty invalidUsername = new SimpleStringProperty("");

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getInvalidLogin() {
        return invalidLogin.get();
    }

    public String getInvalidPassword() {
        return invalidPassword.get();
    }

    public StringProperty invalidPasswordProperty() {
        return invalidPassword;
    }

    public void setInvalidPassword(String invalidPassword) {
        this.invalidPassword.set(invalidPassword);
    }

    public String getInvalidUsername() {
        return invalidUsername.get();
    }

    public StringProperty invalidUsernameProperty() {
        return invalidUsername;
    }

    public void setInvalidUsername(String invalidUsername) {
        this.invalidUsername.set(invalidUsername);
    }

    public StringProperty invalidLoginProperty() {
        return invalidLogin;
    }

    public void setInvalidLogin(String invalidLogin) {
        this.invalidLogin.set(invalidLogin);
    }

    public LoginModel() {
    }

    public LoginModel(String username, String password) {
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

}
