package org.obsys.obsysapp.models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.obsys.obsysapp.domain.Transaction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class AccountModel {
    private final int acctNum;
    private final DoubleProperty interestRate = new SimpleDoubleProperty(-1);
    private final DoubleProperty interestDue = new SimpleDoubleProperty(-1);
    private String type = "";
    private double balance = -1;
    private LocalDate dateOpened = LocalDate.MIN;
    private String status = "";
    private double loanAmt = -1;
    private int term = -1;
    private double interestPaid = -1;
    private double installment = -1;
    private LocalDate selectedMonth;
    private ArrayList<Transaction> history = new ArrayList<>();

    public AccountModel(int acctNum) {
        this.acctNum = acctNum;
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

    /**
     * Sends to the view the most recent 7 days of transactions.
     *
     * @return list of transaction within one week of today
     */
    public ArrayList<String> getPendingTransactions() {
        ArrayList<String> transactions = new ArrayList<>();

        String date;
        String description;
        String credit;
        String debit;

        for (Transaction t : history) {
            if (t.getDate().isAfter(LocalDate.now().minusWeeks(1))) {
                date = t.getDate().format(DateTimeFormatter.ofPattern("MM/dd/yy"));
                description = getPayeeDescription(t);

                if (t.getAmount() > 0) {
                    credit = String.format("$%,.2f", t.getAmount());
                    debit = "-";
                } else {
                    debit = String.format("$%,.2f", t.getAmount() * -1);
                    credit = "-";
                }

                transactions.add(String.format("%-9s %-20s %9s %9s \n", date, description, credit, debit));
            }
        }
        return transactions;

    }

    /***
     * Sends transaction beyond a week old to 1 month.
     * Rather than limit the query to 1 month, we parse it out here. This allows us to get full history details such as
     * total interest accrued without having to run 2 seperate queries.
     * @return a list of transactions from 7 to 30 days old
     */
    public ArrayList<String> getPostedTransactions() {
        ArrayList<String> transactions = new ArrayList<>();

        String date;
        String description;
        String credit;
        String debit;

        for (Transaction t : history) {
            if (t.getDate().isBefore(LocalDate.now().minusDays(6)) &&
                    t.getDate().isAfter(LocalDate.now().minusMonths(1))) {
                date = t.getDate().format(DateTimeFormatter.ofPattern("MM/dd/yy"));
                description = getPayeeDescription(t);

                if (t.getAmount() > 0) {
                    credit = String.format("$%,.2f", t.getAmount());
                    debit = "-";
                } else {
                    debit = String.format("$%,.2f", t.getAmount() * -1);
                    credit = "-";
                }

                transactions.add(String.format("%-9s %-20s %9s %9s \n", date, description, credit, debit));
            }
        }
        return transactions;
    }

    public ArrayList<String> getScheduledLoanPayments() {
        ArrayList<String> payments = new ArrayList<>();
        String date;

        for (Transaction t : history) {
            if (t.getDate().isAfter(LocalDate.now().minusWeeks(1))) {
                date = t.getDate().format(DateTimeFormatter.ofPattern("MM/dd/yy"));
                payments.add(String.format("%-30s $%,-10.2f", date, t.getAmount()));
            }
        }
        return payments;
    }

    public ArrayList<String> getPostedLoanPayments() {
        ArrayList<String> payments = new ArrayList<>();
        String date;

        for (Transaction t : history) {
            if (t.getDate().isBefore(LocalDate.now().minusDays(6)) &&
                    t.getDate().isAfter(LocalDate.now().minusMonths(6))) {
                date = t.getDate().format(DateTimeFormatter.ofPattern("MM/dd/yy"));
                payments.add(String.format("%-10s $%,-11.2f $%,-11.2f $%,-11.2f",
                        date, Math.abs(t.getAmount()), t.getAmtToPrincipal(), t.getAmtToInterest()));
            }
        }
        return payments;
    }

    public StringProperty dateOpenedProperty() {
        return new SimpleStringProperty(dateOpened.format(DateTimeFormatter.ofPattern("MM/dd/yy")));
    }

    public StringProperty maturityProperty() {
        return new SimpleStringProperty(dateOpened.plusMonths(term).format(DateTimeFormatter.ofPattern("MM/dd/yy")));
    }

    public StringProperty remainingPaymentsCountProperty() {
        LocalDate maturity = dateOpened.plusMonths(term);
        LocalDate current = getCurrentDueDate();

        long months = ChronoUnit.MONTHS.between(current, maturity);

        return new SimpleStringProperty(String.valueOf(months));
    }

    private LocalDate getCurrentDueDate() {
        LocalDate currentDueDate = dateOpened.withMonth(LocalDate.now().getMonthValue());
        currentDueDate = currentDueDate.withYear(LocalDate.now().getYear());

        if (currentDueDate.isBefore(LocalDate.now())) {
            currentDueDate = currentDueDate.plusMonths(1);
        }
        return currentDueDate;
    }

    public StringProperty dueDateProperty() {
        return new SimpleStringProperty(getCurrentDueDate().format(DateTimeFormatter.ofPattern("MM/dd/yy")));
    }

    public StringProperty interestRateProperty() {
        return new SimpleStringProperty(interestRate.get() + "%");
    }

    public StringProperty interestPaymentsProperty() {
        double totalPaid = 0;
        for (Transaction t : history) {
            if (t.getType().equals("DP") && t.getPayee().equals("Interest Payment")) {
                totalPaid += t.getAmount();
            }
        }
        return new SimpleStringProperty(String.format("$%,.2f", totalPaid));
    }

    public StringProperty paidInterestProperty() {
        return new SimpleStringProperty(String.format("$%,.2f", interestPaid));
    }

    public StringProperty paidPrincipalProperty() {
        return new SimpleStringProperty(String.format("$%,.2f", loanAmt - balance));
    }

    public StringProperty installmentProperty() {
        return new SimpleStringProperty(String.format("$%,.2f", installment));
    }

    public StringProperty loanAmountProperty() {
        return new SimpleStringProperty(String.format("$%,.2f", loanAmt));
    }

    public int getAcctNum() {
        return acctNum;
    }

    public double getInterestRate() {
        return interestRate.get();
    }

    public void setInterestRate(double interestRate) {
        this.interestRate.set(interestRate);
    }

    public double getInterestDue() {
        return interestDue.get();
    }

    public void setInterestDue(double interestDue) {
        this.interestDue.set(interestDue);
    }

    public DoubleProperty interestDueProperty() {
        return interestDue;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public LocalDate getDateOpened() {
        return dateOpened;
    }

    public void setDateOpened(LocalDate dateOpened) {
        this.dateOpened = dateOpened;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getLoanAmt() {
        return loanAmt;
    }

    public void setLoanAmt(double loanAmt) {
        this.loanAmt = loanAmt;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public double getInterestPaid() {
        return interestPaid;
    }

    public void setInterestPaid(double interestPaid) {
        this.interestPaid = interestPaid;
    }

    public double getInstallment() {
        return installment;
    }

    public void setInstallment(double installment) {
        this.installment = installment;
    }

    public ArrayList<Transaction> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<Transaction> history) {
        this.history = history;
    }

    public ArrayList<LocalDate> getMonths() {
        ArrayList<LocalDate> months = new ArrayList<>();

        if (history.isEmpty()) {
            return months;
        }

        history.sort(Comparator.comparing(Transaction::getDate));
        long numberOfMonths = ChronoUnit.MONTHS.between(dateOpened, history.getLast().getDate());

        for (int i = 0; i < numberOfMonths; i++) {
            if (dateOpened.plusMonths(i + 1).isBefore(LocalDate.now())) {
                months.add(dateOpened.plusMonths(i + 1));
            }
        }
        Collections.sort(months, Collections.reverseOrder());
        return months;
    }

    public LocalDate getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(LocalDate selectedMonth) {
        this.selectedMonth = selectedMonth;
    }
}
