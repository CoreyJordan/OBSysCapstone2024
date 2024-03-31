package org.obsys.obsysapp.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.obsys.obsysapp.domain.Account;
import org.obsys.obsysapp.domain.Payee;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TransactionModel {
    private final String transactionType; // Types currently include: DP, WD, TF, PY, RC
    private final Account account;
    private final ArrayList<Payee> payees;
    private final StringProperty transactionPayee = new SimpleStringProperty("");
    private final StringProperty transactionAmount = new SimpleStringProperty("0.00");
    private final ObjectProperty<LocalDate> transactionDate = new SimpleObjectProperty<>(LocalDate.now());
    private final StringProperty payeeError = new SimpleStringProperty("");
    private final StringProperty amountError = new SimpleStringProperty("");


    public TransactionModel(String transactionType, Account account, ArrayList<Payee> payees) {
        this.transactionType = transactionType;
        this.account = account;
        this.payees = payees;
    }

    // PROPERTIES
    public StringProperty transactionTypeProperty() {
        return switch (transactionType) {
            case "DP" -> new SimpleStringProperty("Deposit Funds");
            case "WD" -> new SimpleStringProperty("Withdraw Funds");
            case "TF" -> new SimpleStringProperty("Transfer Funds");
            default -> new SimpleStringProperty("Make a Payment");
        };
    }

    public StringProperty accountTypeProperty() {
        return new SimpleStringProperty(switch (account.getType()) {
            case "CH" -> "Checking";
            case "IC" -> "Checking+";
            case "LN" -> "Loan";
            case "SV" -> "Saving";
            default -> throw new IllegalStateException("Unexpected value: " + account.getType());
        });
    }

    public StringProperty accountNumProperty() {
        String acctNum = "..." + String.valueOf(account.getAcctNum()).substring(6);
        return new SimpleStringProperty(acctNum);
    }

    public StringProperty balanceProperty() {
        String balance = String.format("$%,.2f", account.getBalance());
        return new SimpleStringProperty(balance);
    }

    public StringProperty balanceTypeProperty() {
        if (account.getType().equals("LN")) {
            return new SimpleStringProperty("BALANCE");
        }
        return new SimpleStringProperty("AVAILABLE");
    }

    public StringProperty statusProperty() {
        return new SimpleStringProperty(switch (account.getStatus()) {
            case "DQ" -> "This account is delinquent";
            case "CL" -> "This account is closed";
            case "SU" -> "This account is suspended";
            default -> "";
        });
    }

    public StringProperty payDateProperty() {
        if (account.getType().equals("LN")) {
            String paymentDate = "Next Payment: ";

            int dayDue = account.getPaymentDate().getDayOfMonth();
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

    public StringProperty amountDueProperty() {
        if (account.getType().equals("LN")) {
            String amountDue = "Amount due: ";
            amountDue += String.format("$%,.2f", account.getPaymentAmt());
            return new SimpleStringProperty(amountDue);
        }
        return new SimpleStringProperty("");
    }

    public StringProperty payeeTypeProperty() {
        return new SimpleStringProperty(switch (transactionType) {
            case "WD" -> "PAYEE";
            case "TF" -> "TO ACCOUNT";
            case "PY" -> "FROM ACCOUNT";
            default -> "PAYER";
        });
    }

    public StringProperty transactionPayeeProperty() {
        return transactionPayee;
    }

    public StringProperty transactionAmountProperty() {
        return transactionAmount;
    }

    public ObjectProperty<LocalDate> transactionDateProperty() {
        return transactionDate;
    }

    public StringProperty actionProperty() {
        return new SimpleStringProperty(switch (transactionType) {
            case "WD" -> "Withdraw";
            case "TF" -> "Transfer";
            case "PY" -> "Pay";
            default -> "Deposit";
        });
    }

    public StringProperty payeeErrorProperty() {
        return payeeError;
    }

    public StringProperty amountErrorProperty() {
        return amountError;
    }

    // GETTERS
    public ArrayList<String> getPayeeDescriptions() {
        ArrayList<String> payeeDescriptions = new ArrayList<>();
        // First item is blank for user editing.
        if (transactionType.equals("DP") || transactionType.equals("WD")) {
            payeeDescriptions.addFirst("");
        }

        for (Payee p : payees) {
            payeeDescriptions.add(p.getDescription());
        }
        return payeeDescriptions;
    }

    public ArrayList<Payee> getPayees() {
        return payees;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getTransactionPayee() {
        return transactionPayee.get();
    }

    public String getTransactionAmount() {
        return transactionAmount.get();
    }

    public Account getAccount() {
        return account;
    }

    public LocalDate getTransactionDate() {
        return transactionDate.get();
    }

    // SETTERS

    public void setPayeeError(String payeeError) {
        this.payeeError.set(payeeError);
    }

    public void setAmountError(String amountError) {
        this.amountError.set(amountError);
    }

}
