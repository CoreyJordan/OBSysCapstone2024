package org.obsys.obsysapp.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.obsys.obsysapp.domain.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class StatementModel {
    private ArrayList<LocalDate> months = new ArrayList<>();
    private boolean isValidMonth;
    private Person person;
    private Account account;
    private LocalDate selectedMonth = LocalDate.MIN;
    private MonthlySummary summary;

    public StatementModel(Person person, Account account) {
        this.person = person;
        this.account = account;
    }

    public StatementModel(Person person, Account account, MonthlySummary summary, LocalDate selectedMonth) {
        this(person, account);
        this.summary = summary;
        this.selectedMonth = selectedMonth;
    }



    private static String getPayeeDescription(Transaction t) {
        String description;
        switch (t.getType()) {
            case "TF":
                description = "To:" + t.getTransferToAcctId();
                break;
            case "RC":
                description = "From: " + t.getTransferToAcctId();
                break;
            case "PY":
                description = "Loan:" + t.getTransferToAcctId();
                break;
            default:
                description = t.getPayee();
                int end = description.length();
                if (end > 18) {
                    end = 18;
                }
                description = description.substring(0, end);
        }
        return description;
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
                getStartDate(),
                geteEndDate()
        );
        return new SimpleStringProperty(period);
    }

    private String getStartDate() {
        return selectedMonth
                .minusMonths(1)
                .plusDays(1)
                .format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }

    private String geteEndDate() {
        return selectedMonth.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }

    public StringProperty summaryProperty() {
        return new SimpleStringProperty(String.format(
                "%-40s %,12.2f \n%-40s %,12.2f \n%-40s %,12.2f \n%-40s %,12.2f \n%-40s %,12.2f \n%-40s %,12.2f \n",
                "BEGINNING BALANCE - " + getStartDate(), summary.getBalanceBegin(),
                "CREDITS", summary.getCredits(),
                "DEBITS", Math.abs(summary.getDebits()),
                "INTEREST PAYMENTS", summary.getInterestPaid(),
                "FEES", summary.getFees(),
                "ENDING BALANCE - " + geteEndDate(), summary.getBalanceEnd()
        ));
    }

    public StringProperty transactionsProperty() {
        String date;
        String description;
        String credit;
        String debit;

        String transactionList = String.format("%-9s %-20s %9s %9s %9s\n", "Date", "Description", "Credit", "Debit", "Balance");
        for (Transaction t : summary.getTransactions()) {
            date = t.getDate().format(DateTimeFormatter.ofPattern("MM/dd/yy"));
            description = getPayeeDescription(t);

            if (t.getAmount() > 0) {
                credit = String.format("$%,.2f", t.getAmount());
                debit = "-";
            } else {
                debit = String.format("$%,.2f", t.getAmount() * -1);
                credit = "-";
            }

            transactionList += String.format("%-9s %-20s %9s %9s %9s\n", date, description, credit, debit, t.getBalance());
        }

        return new SimpleStringProperty(transactionList);
    }

    // GETTERS
    public ArrayList<LocalDate> getMonths() {
        return months;
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

    public Login getLogin() {
        return person.getLogin();
    }

    public LocalDate getSelectedMonth() {
        return selectedMonth;
    }

    public boolean isValidMonth() {
        return isValidMonth;
    }

    // SETTERS
    public void setMonths(ArrayList<LocalDate> months) {
        this.months = months;
    }

    public void setValidMonth(boolean validMonth) {
        isValidMonth = validMonth;
    }

    public void setSelectedPeriod(LocalDate selectedPeriod) {
        this.selectedMonth = selectedPeriod;
    }

}
