package org.obsys.obsysapp.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.obsys.obsysapp.domain.Account;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AccountsModel {
    private final ArrayList<Account> accounts;
    private int targetAccountNumber;

    public int getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public void setTargetAccountNumber(int targetAccountNumber) {
        this.targetAccountNumber = targetAccountNumber;
    }

    public AccountsModel(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public int accountsCount() {
        return accounts.size();
    }

    public StringProperty getAccountType(int i) {

        return new SimpleStringProperty(switch (accounts.get(i).getType()) {
            case "CH" -> "Checking";
            case "IC" -> "Checking+";
            case "LN" -> "Loan";
            case "SV" -> "Saving";
            default -> throw new IllegalStateException("Unexpected value: " + accounts.get(i).getType());
        });
    }

    public StringProperty getAccountNumLast4(int i) {
        String acctNum = "..." + String.valueOf(accounts.get(i).getAcctNum()).substring(6);
        return new SimpleStringProperty(acctNum);
    }

    public int getAcctNum(int i) {
        return accounts.get(i).getAcctNum();
    }

    public StringProperty getBalance(int i) {
        String balance = String.format("$%,.2f", accounts.get(i).getBalance());
        return new SimpleStringProperty(balance);
    }

    public StringProperty balanceTypeProperty(int i) {
        SimpleStringProperty label = new SimpleStringProperty("Available");
        if (accounts.get(i).getType().equals("LN")) {
            label.set("BALANCE");
        }
        return label;
    }

    public StringProperty statusProperty(int i) {
        return new SimpleStringProperty(switch (accounts.get(i).getStatus()) {
            case "DQ" -> "This account is delinquent";
            case "CL" -> "This account is closed";
            case "SU" -> "This account is suspended";
            default -> "";
        });
    }

    /**
     * Produces the next pay date for an upcoming loan payment. Does not return year. Payment date is based directly
     * from date the account was opened.
     * @param i the index of the loan account within the list
     * @return the next day and month for payments due
     */
    public StringProperty payDateProperty(int i) {
        if (accounts.get(i).getType().equals("LN")) {
            String paymentDate = "Next Payment: ";

            int dayDue = accounts.get(i).getPaymentDate().getDayOfMonth();
            int month = LocalDate.now().getMonthValue();

            if (LocalDate.now().getDayOfMonth() > dayDue) {
                month++;
            }

            LocalDate next = LocalDate.of(LocalDate.now().getYear(), month, dayDue);
            paymentDate += next.format(DateTimeFormatter.ofPattern("MM/dd"));
            return new SimpleStringProperty(paymentDate);
        }
        return new SimpleStringProperty("");
    }

    public StringProperty amountDueProperty(int i) {
        if (accounts.get(i).getPaymentAmt() != -1) {
            String amountDue = "Amount due: ";
            amountDue += String.format("$%,.2f", accounts.get(i).getPaymentAmt());
            return new SimpleStringProperty(amountDue);
        }
        return new SimpleStringProperty("");
    }
}
