package org.obsys.obsysapp.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NewAccountModel {
    private final LocalDate currentDate = LocalDate.now();
    private BooleanProperty newCustomerFieldsDisabled = new SimpleBooleanProperty(true);
    // TODO add textfield properties to list
    private ArrayList<StringProperty> textfields = new ArrayList<>();
    private ArrayList<String> states = new ArrayList<>();
    private ArrayList<String> types = new ArrayList<>(List.of("Checking", "Savings", "Checking +", "Loan"));
    private ArrayList<String> terms = new ArrayList<>(List.of("36 mo", "42 mo", "48 mo", "54 mo", "60 mo", "66 mo"));

    public NewAccountModel() {
        listStates();
    }

    public StringProperty dateProperty() {
        return new SimpleStringProperty(currentDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    }

    public ArrayList<StringProperty> getTextfields() {
        return textfields;
    }

    public boolean isNewCustomerFieldsDisabled() {
        return newCustomerFieldsDisabled.get();
    }

    public void setNewCustomerFieldsDisabled(boolean newCustomerFieldsDisabled) {
        this.newCustomerFieldsDisabled.set(newCustomerFieldsDisabled);
    }

    public BooleanProperty newCustomerFieldsDisabledProperty() {
        return newCustomerFieldsDisabled;
    }

    public ArrayList<String> getStates() {
        return states;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public ArrayList<String> getTerms() {
        return terms;
    }

    private void listStates() {
        ArrayList<String> listOfStates = new ArrayList<>(List.of(
                "AL", "AK", "AZ", "AR", "AS", "CA", "CO", "CT", "DE", "DC", "FL", "GA", "GU", "HI", "ID",
                "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH",
                "NJ", "NM", "NY", "NC", "ND", "MP", "OH", "OK", "OR", "PA", "PR", "RI", "SC", "SD", "TN", "TX", "TT",
                "UT", "VT", "VA", "VI", "WA", "WV", "WI", "WY"
        ));
        states.addAll(listOfStates);
    }
}
