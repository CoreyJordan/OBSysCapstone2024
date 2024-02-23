package org.obsys.obsysapp.models;

import javafx.beans.property.*;

import java.util.ArrayList;

public class PersonModel {
    private IntegerProperty personId = new SimpleIntegerProperty(0);
    private StringProperty firstName = new SimpleStringProperty("");
    private StringProperty lastName = new SimpleStringProperty("");
    private StringProperty streetAddress = new SimpleStringProperty("");
    private StringProperty city = new SimpleStringProperty("");
    private StringProperty state = new SimpleStringProperty("");
    private StringProperty postalCode = new SimpleStringProperty("");
    private StringProperty phone = new SimpleStringProperty("");
    private StringProperty emailAddress = new SimpleStringProperty("");
    private BooleanProperty isAdmin = new SimpleBooleanProperty(false);
    private StringProperty username = new SimpleStringProperty("");
    private StringProperty password = new SimpleStringProperty("");
    private ArrayList<Integer> accountsHeld = new ArrayList<>();

    public int getPersonId() {
        return personId.get();
    }

    public IntegerProperty personIdProperty() {
        return personId;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getStreetAddress() {
        return streetAddress.get();
    }

    public StringProperty streetAddressProperty() {
        return streetAddress;
    }

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public String getState() {
        return state.get();
    }

    public StringProperty stateProperty() {
        return state;
    }

    public String getPostalCode() {
        return postalCode.get();
    }

    public StringProperty postalCodeProperty() {
        return postalCode;
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public String getEmailAddress() {
        return emailAddress.get();
    }

    public StringProperty emailAddressProperty() {
        return emailAddress;
    }

    public boolean isIsAdmin() {
        return isAdmin.get();
    }

    public BooleanProperty isAdminProperty() {
        return isAdmin;
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
