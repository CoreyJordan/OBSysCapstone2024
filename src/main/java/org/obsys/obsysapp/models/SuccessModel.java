package org.obsys.obsysapp.models;

import javafx.beans.property.SimpleStringProperty;
import org.obsys.obsysapp.domain.Account;
import org.obsys.obsysapp.domain.Transaction;

import java.time.format.DateTimeFormatter;

public class SuccessModel {

    private final Transaction transaction;
    private final Account account;

    public SuccessModel(Transaction transaction, Account account) {
        this.transaction = transaction;
        this.account = account;
    }

    public SimpleStringProperty fieldsProperty() {
        return new SimpleStringProperty(switch (transaction.getType()) {
            case "TF" -> "transfer";
            case "PY" -> "payment";
            default -> "Account:\nType:\nAmount\nPayee:\nBalance Pending:\nDate\nRef. #:";
        });
    }

    public SimpleStringProperty detailsProperty() {
        return new SimpleStringProperty(switch (transaction.getType()) {
            case "TF" -> "transfer";
            case "PY" -> "payment";
            default -> String.format("%s \n%s \n%s \n%s \n%s \n%s \n%s \n\n",
                    getAccountName() + " .." + String.valueOf(account.getAcctNum()).substring(6),
                    formatTransactionType(),
                    String.format("$%,.2f", Math.abs(transaction.getAmount())),
                    transaction.getPayee().substring(0, 20),
                    String.format("$%,.2f", transaction.getBalance()),
                    transaction.getDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                    transaction.getReferenceId());
        });
    }

    private String getAccountName() {
        return switch (account.getType()) {
            case "SV" -> "Saving";
            case "LN" -> "Loan";
            case "IC" -> "Checking+";
            default -> "Checking";
        };
    }

    private String formatTransactionType() {
        return switch (transaction.getType()) {
            case "WD" -> "Withdrawal";
            case "TF" -> "transfer";
            case "PY" -> "payment";
            default -> "Deposit";
        };
    }

}
