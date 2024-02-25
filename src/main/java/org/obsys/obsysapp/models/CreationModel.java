package org.obsys.obsysapp.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CreationModel {
    private StringProperty acctNum = new SimpleStringProperty("");
    private StringProperty firstName = new SimpleStringProperty("");
    private StringProperty lastName = new SimpleStringProperty("");

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

    public StringProperty acctNumProperty() {
        return acctNum;
    }

    public void setAcctNum(String acctNum) {
        this.acctNum.set(acctNum);
    }
}
