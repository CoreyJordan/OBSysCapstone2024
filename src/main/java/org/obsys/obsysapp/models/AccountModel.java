package org.obsys.obsysapp.models;

import javafx.beans.property.*;
import org.obsys.obsysapp.domain.Transaction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AccountModel {
    private final int acctNum;
    private String type = "";
    private double balance = -1;
    private LocalDate dateOpened = LocalDate.MIN;
    private String status = "";
    private final DoubleProperty interestRate = new SimpleDoubleProperty(-1);
    private final DoubleProperty loanAmt = new SimpleDoubleProperty(-1);
    private final IntegerProperty term = new SimpleIntegerProperty(-1);
    private final DoubleProperty interestPaid = new SimpleDoubleProperty(-1);
    private final DoubleProperty installment = new SimpleDoubleProperty(-1);
    private ArrayList<Transaction> history = new ArrayList<>();
    private final DoubleProperty interestDue = new SimpleDoubleProperty(-1);


    public AccountModel(int acctNum) {
        this.acctNum = acctNum;
    }

    public void setInstallment(double installment) {
        this.installment.set(installment);
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setDateOpened(LocalDate dateOpened) {
        this.dateOpened = dateOpened;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate.set(interestRate);
    }

    public void setLoanAmt(double loanAmt) {
        this.loanAmt.set(loanAmt);
    }

    public void setTerm(int term) {
        this.term.set(term);
    }

    public void setInterestPaid(double interestPaid) {
        this.interestPaid.set(interestPaid);
    }

    public void setInterestDue(double interestDue) {
        this.interestDue.set(interestDue);
    }


    public void setHistory(ArrayList<Transaction> history) {
        this.history = history;
    }

    public StringProperty typeProperty() {
        return switch (type) {
            case "CH" -> new SimpleStringProperty("Checking");
            case "SV" -> new SimpleStringProperty("Savings");
            case "IC" -> new SimpleStringProperty("Checking+");
            case "LN" -> new SimpleStringProperty("Loan");
            default -> new SimpleStringProperty("Unknown");
        };
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public StringProperty acctNumProperty() {
        return new SimpleStringProperty("..." + String.valueOf(acctNum).substring(6));
    }

    public StringProperty balanceProperty() {
        return new SimpleStringProperty(String.format("$%,.2f", balance));
    }

    public StringProperty balanceTypeProperty() {
        if (type.equals("LN")) {
            return new SimpleStringProperty("BALANCE");
        }
        return new SimpleStringProperty("AVAILABLE");
    }

    public StringProperty statusProperty() {
        return switch (status) {
            case "OP" -> new SimpleStringProperty("");
            case "DQ" -> new SimpleStringProperty("This account is past due");
            case "CL" -> new SimpleStringProperty("This account is closed");
            case "SU" -> new SimpleStringProperty("This account is suspended");
            default -> new SimpleStringProperty("Error getting status");

        };
    }

    public StringProperty postedBalanceProperty() {
        double transactionsTotal = 0;
        for (Transaction t : history) {
            if (t.getDate().isAfter(LocalDate.now().minusWeeks(1))) {
                transactionsTotal += t.getAmount();
            }
        }
        return new SimpleStringProperty(String.format("$%,.2f", balance - transactionsTotal));
    }

    public StringProperty pendingDebitsProperty() {
        double transactionsTotal = 0;
        for (Transaction t : history) {
            if (t.getDate().isAfter(LocalDate.now().minusWeeks(1)) &&
                    !t.getType().equals("DP")) {
                transactionsTotal += t.getAmount() * -1;
            }
        }
        return new SimpleStringProperty(String.format("$%,.2f", transactionsTotal));
    }

    public StringProperty pendingCreditsProperty() {
        double transactionsTotal = 0;
        for (Transaction t : history) {
            if (t.getDate().isAfter(LocalDate.now().minusWeeks(1)) &&
                    t.getType().equals("DP")) {
                transactionsTotal += t.getAmount();
            }

        }
        return new SimpleStringProperty(String.format("$%,.2f", transactionsTotal));
    }

    public ArrayList<String> getPendingTransactions() {
        ArrayList<String> transactions = new ArrayList<>();

        String date;
        String description;
        String credit;
        String debit;

        for (Transaction t : history) {
            if (t.getDate().isAfter(LocalDate.now().minusWeeks(1))) {
                date = t.getDate().format(DateTimeFormatter.ofPattern("MM/dd/yy"));

                if (t.getType().equals("TF")) {
                    description = "To:" + t.getTransferToAcctId();
                } else {
                    description = t.getPayee();
                    int end = description.length();
                    if (end > 15) {
                        end = 15;
                    }
                    description = description.substring(0, end);
                }

                if (t.getAmount() > 0) {
                    credit = String.format("$%,.2f", t.getAmount());
                    debit = "-";
                } else {
                    debit = String.format("$%,.2f", t.getAmount() * -1);
                    credit = "-";
                }

                transactions.add(String.format("%-9s %-18s %-9s %-9s \n", date, description, credit, debit));
            }
        }
        return transactions;

    }

    public ArrayList<String> getPostedTransactions() {
        ArrayList<String> transactions = new ArrayList<>();

        String date;
        String description;
        String credit;
        String debit;

        for (Transaction t : history) {
            if (t.getDate().isBefore(LocalDate.now().minusDays(6))) {
                date = t.getDate().format(DateTimeFormatter.ofPattern("MM/dd/yy"));

                if (t.getType().equals("TF")) {
                    description = "To: " + t.getTransferToAcctId();
                } else {
                    description = t.getPayee();
                    int end = description.length();
                    if (end > 15) {
                        end = 15;
                    }
                    description = description.substring(0, end);
                }

                if (t.getAmount() > 0) {
                    credit = String.format("$%,.2f", t.getAmount());
                    debit = "-";
                } else {
                    debit = String.format("$%,.2f", t.getAmount() * -1);
                    credit = "-";
                }

                transactions.add(String.format("%-9s %-18s %-9s %-9s \n", date, description, credit, debit));
            }
        }
        return transactions;
    }
}
