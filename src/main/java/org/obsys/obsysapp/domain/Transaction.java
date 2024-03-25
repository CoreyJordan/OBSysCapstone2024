package org.obsys.obsysapp.domain;

import java.time.LocalDate;

public class Transaction {
    private final String type;
    private final double amount;
    private final LocalDate date;
    private int accountId;
    private int transferToAcctId;
    private int payeeId;
    private String payee;
    private double amtToPrincipal;
    private double amtToInterest;
    private double balanceResult;

    public Transaction(String type, double amount, LocalDate date, String payee) {
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.payee = payee;
    }

    public Transaction(String type, double amount, LocalDate date,
                       int transferToAcctId, String payee, double amtToPrincipal, double amtToInterest) {
        this(type, amount, date, payee);
        this.transferToAcctId = transferToAcctId;
        this.amtToPrincipal = amtToPrincipal;
        this.amtToInterest = amtToInterest;
    }

    // Transfer Constructor
    public Transaction(String type, double amount, LocalDate date, int accountId, int transferToAcctId) {
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.accountId = accountId;
        this.transferToAcctId = transferToAcctId;
    }

    public Transaction(String type, double amount, LocalDate date, String payee, double balanceResult) {
        this(type, amount, date, payee);
        this.balanceResult = balanceResult;
    }

    public Transaction(String type, double amount, LocalDate date, int transferToAcctId, String payee,
                       double amtToPrincipal, double amtToInterest, double balanceResult) {
        this(type, amount, date, transferToAcctId, payee, amtToPrincipal, amtToInterest);
        this.balanceResult = balanceResult;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public int getTransferToAcctId() {
        return transferToAcctId;
    }

    public String getPayee() {
        return payee;
    }

    public double getAmtToPrincipal() {
        return amtToPrincipal;
    }

    public double getAmtToInterest() {
        return amtToInterest;
    }

    public double getBalance() {
        return balanceResult;
    }

    public int getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(int payeeId) {
        this.payeeId = payeeId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getReferenceId() {
        // TODO query for transactionID
        return 1;
    }
}
