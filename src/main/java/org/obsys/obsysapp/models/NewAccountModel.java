package org.obsys.obsysapp.models;

import javafx.beans.property.*;
import org.obsys.obsysapp.domain.Person;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NewAccountModel {
    private final LocalDate currentDate = LocalDate.now();
    private final ArrayList<String> states = new ArrayList<>();
    private final ArrayList<String> types = new ArrayList<>(
            List.of("Checking", "Savings", "Checking +", "Loan"));
    private final ArrayList<String> terms = new ArrayList<>(
            List.of("36", "42", "48", "54", "60", "66", "180", "360"));
    private final BooleanProperty newCustomerFieldsDisabled =
            new SimpleBooleanProperty(true);
    private final BooleanProperty loanFieldsVisible =
            new SimpleBooleanProperty(false);
    private final BooleanProperty intRateFieldVisible =
            new SimpleBooleanProperty(false);
    private final StringProperty errorMessage = new SimpleStringProperty("");
    // NEW CUSTOMER PROPERTIES
    private final StringProperty newFirstName = new SimpleStringProperty("");
    private final StringProperty newLastName = new SimpleStringProperty("");
    private final StringProperty newAddress = new SimpleStringProperty("");
    private final StringProperty newCity = new SimpleStringProperty("");
    private final ObjectProperty<String> newState =
            new SimpleObjectProperty<>("");
    private final StringProperty newPostal = new SimpleStringProperty("");
    private final StringProperty newPhone = new SimpleStringProperty("");
    private final StringProperty newEmail = new SimpleStringProperty("");
    // CUSTOMER INFO PROPERTIES
    private final StringProperty firstName = new SimpleStringProperty("");
    private final StringProperty lastName = new SimpleStringProperty("");
    private final StringProperty address = new SimpleStringProperty("");
    private final StringProperty email = new SimpleStringProperty("");
    private final StringProperty phone = new SimpleStringProperty("");
    // ACCOUNT PROPERTIES
    private final ObjectProperty<String> acctType =
            new SimpleObjectProperty<>("");
    private final StringProperty balance = new SimpleStringProperty("");
    private final StringProperty balanceType =
            new SimpleStringProperty("STARTING BALANCE");
    private final StringProperty intRate = new SimpleStringProperty("0");
    private final ObjectProperty<String> term = new SimpleObjectProperty<>("");
    private final ArrayList<ObjectProperty<String>> comboBoxes =
            new ArrayList<>(List.of(newState, acctType, term));
    private final StringProperty acctNum = new SimpleStringProperty("");
    private final StringProperty paymentAmt = new SimpleStringProperty("");
    private final StringProperty dueDate = new SimpleStringProperty("");
    private final ArrayList<StringProperty> textFields = new ArrayList<>(
            List.of(newFirstName, newLastName, newAddress, newCity, newPostal,
                    newPhone, newEmail, firstName, lastName, address, email,
                    phone, balance, intRate, acctNum, paymentAmt, dueDate
            ));
    private int personId;

    public NewAccountModel(Person person) {
        listStates();
        personId = person.getPersonId();
        firstName.set(person.getFirstName());
        lastName.set(person.getLastName());
        phone.set(person.getPhone());
        email.set(person.getEmail());
        address.set(String.format("%s\n%s, %s\n%s",
                person.getStreetAddress(),
                person.getCity(),
                person.getState(),
                person.getPostalCode()));
    }

    public NewAccountModel() {
    }

    public StringProperty dateProperty() {
        return new SimpleStringProperty(
                currentDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    }

    public ArrayList<StringProperty> getTextFields() {
        return textFields;
    }

    public boolean isNewCustomerFieldsDisabled() {
        return newCustomerFieldsDisabled.get();
    }

    public void setNewCustomerFieldsDisabled(
            boolean newCustomerFieldsDisabled) {
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

    public StringProperty newFirstNameProperty() {
        return newFirstName;
    }

    public StringProperty newLastNameProperty() {
        return newLastName;
    }

    public StringProperty newAddressProperty() {
        return newAddress;
    }

    public StringProperty newCityProperty() {
        return newCity;
    }

    public StringProperty newPostalProperty() {
        return newPostal;
    }

    public StringProperty newPhoneProperty() {
        return newPhone;
    }

    public StringProperty newEmailProperty() {
        return newEmail;
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty addressProperty() {
        return address;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public String getBalance() {
        return balance.get();
    }

    public void setBalance(String balance) {
        this.balance.set(balance);
    }

    public StringProperty balanceProperty() {
        return balance;
    }

    public ObjectProperty<String> termProperty() {
        return term;
    }

    public StringProperty balanceTypeProperty() {
        return balanceType;
    }

    public String getAcctNum() {
        return acctNum.get();
    }

    public void setAcctNum(String acctNum) {
        this.acctNum.set(acctNum);
    }

    public StringProperty acctNumProperty() {
        return acctNum;
    }

    public StringProperty paymentAmtProperty() {
        return paymentAmt;
    }

    public StringProperty intRateProperty() {
        return intRate;
    }

    public BooleanProperty loanFieldsVisibleProperty() {
        return loanFieldsVisible;
    }

    public BooleanProperty intRateFieldVisibleProperty() {
        return intRateFieldVisible;
    }

    public ObjectProperty<String> newStateProperty() {
        return newState;
    }

    public ObjectProperty<String> acctTypeProperty() {
        return acctType;
    }

    public ArrayList<ObjectProperty<String>> getComboBoxes() {
        return comboBoxes;
    }

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    private void listStates() {
        ArrayList<String> listOfStates = new ArrayList<>(List.of(
                "AL", "AK", "AZ", "AR", "AS", "CA", "CO", "CT", "DE",
                "DC", "FL", "GA", "GU", "HI", "ID", "IL", "IN", "IA", "KS",
                "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT",
                "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "MP", "OH",
                "OK", "OR", "PA", "PR", "RI", "SC", "SD", "TN", "TX", "TT",
                "UT", "VT", "VA", "VI", "WA", "WV", "WI", "WY"
        ));
        states.addAll(listOfStates);
    }

    public String getAcctType() {
        return acctType.get();
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage.set(errorMessage);
    }

    public String getIntRate() {
        return intRate.get();
    }

    public void setLoanFieldsVisible(boolean loanFieldsVisible) {
        this.loanFieldsVisible.set(loanFieldsVisible);
    }

    public void setIntRateFieldVisible(boolean intRateFieldVisible) {
        this.intRateFieldVisible.set(intRateFieldVisible);
    }

    public String getTerm() {
        return term.get();
    }

    public String getNewFirstName() {
        return newFirstName.get();
    }

    public String getNewLastName() {
        return newLastName.get();
    }

    public String getNewAddress() {
        return newAddress.get();
    }

    public String getNewCity() {
        return newCity.get();
    }

    public String getNewState() {
        return newState.get();
    }

    public String getNewPostal() {
        return newPostal.get();
    }

    public String getNewPhone() {
        return newPhone.get();
    }

    public String getNewEmail() {
        return newEmail.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public void setAddress(
            String address, String city, String state, String postal) {
        this.address.set(String.format("%s\n%s, %s\n%s",
                address, city, state, postal));
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public void setBalanceType(String balanceType) {
        this.balanceType.set(balanceType);
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public void setPaymentAmt(String paymentAmt) {
        this.paymentAmt.set(paymentAmt);
    }
}
