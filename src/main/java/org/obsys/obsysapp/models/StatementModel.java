package org.obsys.obsysapp.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.obsys.obsysapp.domain.Account;
import org.obsys.obsysapp.domain.MonthlySummary;
import org.obsys.obsysapp.domain.Person;
import org.obsys.obsysapp.domain.Transaction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StatementModel {
    private final Person person;
    private final Account account;
    private LocalDate selectedMonth = LocalDate.MIN;
    private MonthlySummary summary;

    public StatementModel(Person person, Account account) {
        this.person = person;
        this.account = account;
    }

    public StatementModel(Person person,
                          Account account,
                          MonthlySummary summary,
                          LocalDate selectedMonth) {
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
        return new SimpleStringProperty(
                selectedMonth.format(
                        DateTimeFormatter.ofPattern("MMM dd, yyyy")));
    }

    public StringProperty stmtPeriodProperty() {
        String period = String.format("%s to %s",
                getStartDate(),
                getEndDate()
        );
        return new SimpleStringProperty(period);
    }

    private String getStartDate() {
        return selectedMonth
                .minusMonths(1)
                .plusDays(1)
                .format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }

    private String getEndDate() {
        return selectedMonth.format(
                DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }

    public StringProperty summaryProperty() {
        return new SimpleStringProperty(String.format(
                """
                        %-40s %,12.2f\s
                        %-40s %,12.2f\s
                        %-40s %,12.2f\s
                        %-40s %,12.2f\s
                        %-40s %,12.2f\s
                        %-40s %,12.2f\s
                        """,
                "BEGINNING BALANCE - " + getStartDate(),
                summary.getBalanceBegin(),
                "CREDITS",
                summary.getCredits(),
                "DEBITS",
                Math.abs(summary.getDebits()),
                "INTEREST PAYMENTS",
                summary.getInterestPaid(),
                "FEES",
                summary.getFees(),
                "ENDING BALANCE - " + getEndDate(),
                summary.getBalanceEnd()
        ));
    }

    public StringProperty transactionsProperty() {
        String date;
        String description;
        String credit;
        String debit;

        StringBuilder transactionList = new StringBuilder(
                String.format("%-9s %-20s %9s %9s %9s\n",
                        "Date", "Description", "Credit", "Debit", "Balance"));
        for (Transaction t : summary.getTransactions()) {
            date = DateTimeFormatter.ofPattern("MM/dd/yy").format(t.getDate());
            description = getPayeeDescription(t);

            if (t.getAmount() > 0) {
                credit = String.format("$%,.2f", t.getAmount());
                debit = "-";
            } else {
                debit = String.format("$%,.2f", t.getAmount() * -1);
                credit = "-";
            }

            transactionList.append(
                    String.format("%-9s %-20s %9s %9s %9s\n",
                            date, description, credit, debit, t.getBalance()));
        }

        return new SimpleStringProperty(transactionList.toString());
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


}
