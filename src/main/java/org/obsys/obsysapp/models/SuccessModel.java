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
            case "TF" -> """
                    Account:
                    Type:
                    Amount
                    To Account:
                    Balance Pending:
                    Date
                    Ref. #:""";
            case "PY" -> """
                    Account:
                    Type:
                    Amount
                    Balance:
                    Date
                    Ref. #:""";
            default -> """
                    Account:
                    Type:
                    Amount
                    Payee:
                    Balance Pending:
                    Date
                    Ref. #:""";
        });
    }

    public SimpleStringProperty detailsProperty() {
        if (transaction.getType().equals("PY")) {
            return new SimpleStringProperty(
                    String.format("%s \n%s \n%s \n%s \n%s \n%s \n\n",
                            getAccountName() + " .." + String.valueOf(
                                    account.getAcctNum()).substring(6),
                            formatTransactionType(),
                            String.format("$%,.2f",
                                    Math.abs(transaction.getAmount())),
                            String.format("$%,.2f",
                                    transaction.getBalance()),
                            transaction.getDate().format(
                                    DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                            transaction.getReferenceId()));
        }

        return new SimpleStringProperty(
                String.format("%s \n%s \n%s \n%s \n%s \n%s \n%s \n\n",
                        getAccountName() + " .." + String.valueOf(
                                account.getAcctNum()).substring(6),
                        formatTransactionType(),
                        String.format("$%,.2f",
                                Math.abs(transaction.getAmount())),
                        truncatePayee(transaction.getPayee()),
                        String.format("$%,.2f", transaction.getBalance()),
                        transaction.getDate().format(
                                DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                        transaction.getReferenceId()));
    }

    private String truncatePayee(String payee) {
        if (payee.contains("$")) {
            payee = payee.substring(0, payee.indexOf("$"));
        }

        if (payee.length() > 20) {
            return payee.substring(0, 20);
        }
        return payee;
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
            case "TF" -> "Transfer";
            case "PY" -> "Payment";
            default -> "Deposit";
        };
    }

    public Account getAccount() {
        return account;
    }
}
