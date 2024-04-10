package org.obsys.obsysapp.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.obsys.obsysapp.domain.Payee;
import org.obsys.obsysapp.domain.Person;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdminHomeModel {
    private final StringProperty username;
    private final LocalDate currentDate = LocalDate.now();
    private final StringProperty accountNumber = new SimpleStringProperty("");
    private final StringProperty searchFirstName = new SimpleStringProperty("");
    private final StringProperty searchLastName = new SimpleStringProperty("");
    private final StringProperty foundFirstName = new SimpleStringProperty("");
    private final StringProperty foundLastName = new SimpleStringProperty("");
    private final StringProperty searchError = new SimpleStringProperty("");
    private final StringProperty accountStatus = new SimpleStringProperty("");
    private final StringProperty address = new SimpleStringProperty("");
    private final StringProperty phone = new SimpleStringProperty("");
    private final StringProperty email = new SimpleStringProperty("");
    private final ArrayList<StringProperty> textFields = new ArrayList<>(
            List.of(accountNumber, searchFirstName, searchLastName,
                    foundFirstName, foundLastName, searchError, accountStatus,
                    address, phone, email));
    private final ObservableList<String> acctDescriptions =
            FXCollections.observableArrayList();
    private final BooleanProperty actionPanelDisabled =
            new SimpleBooleanProperty(true);
    private ArrayList<Payee> accounts = new ArrayList<>();
    private int selectedAccount = 0;
    private AccountModel accountModel = new AccountModel();
    private int foundPersonId = 0;


    public AdminHomeModel(String username) {
        this.username = new SimpleStringProperty("Welcome " + username + "!");
    }


    public StringProperty dateTimeProperty() {
        return new SimpleStringProperty(
                currentDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public String getAccountNumber() {
        return accountNumber.get();
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber.set(accountNumber);
    }

    public StringProperty accountNumberProperty() {
        return accountNumber;
    }

    public String getSearchFirstName() {
        return searchFirstName.get();
    }

    public void setSearchFirstName(String searchFirstName) {
        this.searchFirstName.set(searchFirstName);
    }

    public StringProperty searchFirstNameProperty() {
        return searchFirstName;
    }

    public String getSearchLastName() {
        return searchLastName.get();
    }

    public void setSearchLastName(String searchLastName) {
        this.searchLastName.set(searchLastName);
    }

    public StringProperty searchLastNameProperty() {
        return searchLastName;
    }

    public void setFoundFirstName(String foundFirstName) {
        this.foundFirstName.set(foundFirstName);
    }

    public StringProperty foundFirstNameProperty() {
        return foundFirstName;
    }

    public void setFoundLastName(String foundLastName) {
        this.foundLastName.set(foundLastName);
    }

    public StringProperty foundLastNameProperty() {
        return foundLastName;
    }

    public void setSearchError(String searchError) {
        this.searchError.set(searchError);
    }

    public StringProperty searchErrorProperty() {
        return searchError;
    }

    public ArrayList<StringProperty> getTextFields() {
        return textFields;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setAccounts(ArrayList<Payee> accounts) {
        this.accounts = accounts;
    }

    public void setPerson(Person customer) {
        setFoundLastName(customer.getLastName());
        setFoundFirstName(customer.getFirstName());
        setPhone(customer.getPhone());
        setEmail(customer.getEmail());
        setAddress(String.format("%s\n%s, %s\n%s",
                customer.getStreetAddress(),
                customer.getCity(),
                customer.getState(),
                customer.getPostalCode()));
        foundPersonId = customer.getPersonId();
    }

    public int getFoundPersonId() {
        return foundPersonId;
    }

    public void getAccountDescriptions() {
        acctDescriptions.clear();
        for (Payee p : accounts) {
            acctDescriptions.add(p.description());
        }
    }

    public ObservableList<String> getAcctDescriptions() {
        return acctDescriptions;
    }

    public void clearAcctDescriptions() {
        acctDescriptions.clear();
    }

    public void setSelectedAccountDescription(String selectedDescription) {
        for (Payee p : accounts) {
            if (p.description().equals(selectedDescription)) {
                selectedAccount = p.payeeNumber();
            }
        }
    }

    public int getSelectedAccount() {
        return selectedAccount;
    }

    public void setSelectedAccount(int selectedAccount) {
        this.selectedAccount = selectedAccount;
    }

    public AccountModel getAccountModel() {
        return accountModel;
    }

    public void setAccountModel(AccountModel accountModel) {
        this.accountModel = accountModel;
    }

    public BooleanProperty actionPanelDisabledProperty() {
        return actionPanelDisabled;
    }

    public void setActionPanelDisabled(boolean actionPanelDisabled) {
        this.actionPanelDisabled.set(actionPanelDisabled);
    }
}
