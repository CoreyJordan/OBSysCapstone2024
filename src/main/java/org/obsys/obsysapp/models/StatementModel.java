package org.obsys.obsysapp.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.obsys.obsysapp.domain.Account;
import org.obsys.obsysapp.domain.Login;
import org.obsys.obsysapp.domain.Person;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class StatementModel {
    private ArrayList<LocalDate> months = new ArrayList<>();
    private boolean isValidMonth;
    private Person person;
    private Account account;
    private LocalDate selectedMonth = LocalDate.MIN;

    public StatementModel(Person person, Account account) {
        this.person = person;
        this.account = account;
    }

    // PROPERTIES
    public StringProperty accountNumProperty() {
        return new SimpleStringProperty(String.valueOf(account.getAcctNum()));
    }

    public StringProperty stmtDateProperty() {
        return new SimpleStringProperty(selectedMonth.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
    }

    public StringProperty stmtPeriodProperty() {
        String period = String.format("%s to %s",
                selectedMonth
                        .minusMonths(1)
                        .plusDays(1)
                        .format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                selectedMonth.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
        );
        return new SimpleStringProperty(period);
    }

    // GETTERS
    public ArrayList<LocalDate> getMonths() {
        return months;
    }

    public boolean isValidMonth() {
        return isValidMonth;
    }

    public String getNameAndAddress() {
        return String.format("%s %s\n%s\n%s, %s %s",
                person.getFirstName(),
                person.getLastName(),
                person.getStreetAddress(),
                person.getCity(),
                person.getState(),
                person.getPostalCode());
    }

    public int getAccountNumber() {
        return account.getAcctNum();
    }

    public Login getLogin() {
        return person.getLogin();
    }

    // SETTERS
    public void setMonths(ArrayList<LocalDate> months) {
        this.months = months;
    }

    public void setSelectedPeriod(LocalDate selectedPeriod) {
        this.selectedMonth = selectedPeriod;
    }

    public void setValidMonth(boolean validMonth) {
        isValidMonth = validMonth;
    }
}
