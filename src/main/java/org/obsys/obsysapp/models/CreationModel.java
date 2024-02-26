package org.obsys.obsysapp.models;

import javafx.beans.property.*;

public class CreationModel {
    private StringProperty acctNum = new SimpleStringProperty("");
    private StringProperty firstName = new SimpleStringProperty("");
    private StringProperty lastName = new SimpleStringProperty("");
    private StringProperty acctNumWarning = new SimpleStringProperty("");
    private StringProperty firstNameWarning = new SimpleStringProperty("");
    private StringProperty lastNameWarning = new SimpleStringProperty("");
    private StringProperty notFound = new SimpleStringProperty("");

    public String getNotFound() {
        return notFound.get();
    }

    public StringProperty notFoundProperty() {
        return notFound;
    }

    public void setNotFound(String notFound) {
        this.notFound.set(notFound);
    }

    public String getFirstNameWarning() {
        return firstNameWarning.get();
    }

    public StringProperty firstNameWarningProperty() {
        return firstNameWarning;
    }

    public void setFirstNameWarning(String firstNameWarning) {
        this.firstNameWarning.set(firstNameWarning);
    }

    public String getLastNameWarning() {
        return lastNameWarning.get();
    }

    public StringProperty lastNameWarningProperty() {
        return lastNameWarning;
    }

    public void setLastNameWarning(String lastNameWarning) {
        this.lastNameWarning.set(lastNameWarning);
    }

    public String getAcctNumWarning() {
        return acctNumWarning.get();
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

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getAcctNum() {
        return acctNum.get();
    }

    public Property<String> acctNumProperty() {
        return acctNum;
    }

    public void setAcctNum(String acctNum) {
        this.acctNum.set(acctNum);
    }
}
