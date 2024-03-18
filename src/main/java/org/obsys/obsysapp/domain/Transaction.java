package org.obsys.obsysapp.domain;

import java.time.LocalDate;

public class Transaction {
    private final String type;
    private double amount;
    private final LocalDate date;
    private int transferToAcctId;
    private final String payee;
    private double amtToPrincipal;
    private double amtToInterest;
    private double balanceResult;

    public Transaction(String type, double amount, LocalDate date, String payee) {
        this.type = type;

        // Only deposits remain positive
        this.amount = amount;
        if (!type.equals("DP")) {
            this.amount *= -1;
        }

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

    public Transaction(String type, double amount, LocalDate date, String payee, double balanceResult) {
        this(type, amount, date, payee);
        this.balanceResult = balanceResult;
    }

    public Transaction(String type, double amount, LocalDate date, int transferToAcctId, String payee,
                       double amtToPrincipal, double amtToInterest, double balanceResult) {
        this(type, amount, date, transferToAcctId, payee, amtToPrincipal, amtToInterest);
        this.balanceResult = balanceResult;
    }

    // Getters
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
}
