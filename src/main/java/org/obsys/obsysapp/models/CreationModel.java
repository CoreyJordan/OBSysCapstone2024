package org.obsys.obsysapp.models;

import javafx.beans.property.*;

public class CreationModel {
    private final StringProperty acctNum = new SimpleStringProperty("");
    private final StringProperty firstName = new SimpleStringProperty("");
    private final StringProperty lastName = new SimpleStringProperty("");
    private final StringProperty acctNumWarning = new SimpleStringProperty("");
    private final StringProperty firstNameWarning = new SimpleStringProperty("");
    private final StringProperty lastNameWarning = new SimpleStringProperty("");
    private final StringProperty notFound = new SimpleStringProperty("");
    private final BooleanProperty registrationDisabled = new SimpleBooleanProperty(true);
    private final BooleanProperty passwordDisable = new SimpleBooleanProperty(true);


    public BooleanProperty registrationDisabledProperty() {
        return registrationDisabled;
    }

    public void setRegistrationDisabled(boolean registrationDisabled) {
        this.registrationDisabled.set(registrationDisabled);
    }

    public BooleanProperty passwordDisableProperty() {
        return passwordDisable;
    }

    public void setPasswordDisable(boolean passwordDisable) {
        this.passwordDisable.set(passwordDisable);
    }

    public StringProperty notFoundProperty() {
        return notFound;
    }

    public void setNotFound(String notFound) {
        this.notFound.set(notFound);
    }

    public StringProperty firstNameWarningProperty() {
        return firstNameWarning;
    }

    public void setFirstNameWarning(String firstNameWarning) {
        this.firstNameWarning.set(firstNameWarning);
    }

    public StringProperty lastNameWarningProperty() {
        return lastNameWarning;
    }

    public void setLastNameWarning(String lastNameWarning) {
        this.lastNameWarning.set(lastNameWarning);
    }

    public StringProperty acctNumWarningProperty() {
        return acctNumWarning;
    }

    public void setAcctNumWarning(String acctNumWarning) {
        this.acctNumWarning.set(acctNumWarning);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getAcctNum() {
        return acctNum.get();
    }

    public void setAcctNum(String acctNum) {
        this.acctNum.set(acctNum);
    }

    public Property<String> acctNumProperty() {
        return acctNum;
    }
}
