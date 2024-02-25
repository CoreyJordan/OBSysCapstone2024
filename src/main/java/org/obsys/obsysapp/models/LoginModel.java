package org.obsys.obsysapp.models;

import javafx.beans.property.*;

public class LoginModel {
    private StringProperty username = new SimpleStringProperty("");
    private StringProperty password = new SimpleStringProperty("");
    private final StringProperty invalidLogin = new SimpleStringProperty("");


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
