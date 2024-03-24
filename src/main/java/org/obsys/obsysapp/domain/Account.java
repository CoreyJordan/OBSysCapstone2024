package org.obsys.obsysapp.domain;

import java.time.LocalDate;

public class Account {
    private final String type;
    private final int acctNum;
    private final String status;
    private final double balance;
    private final LocalDate paymentDate;
    private double paymentAmt;

    public Account(String type, int acctNum, String status, double balance, LocalDate paymentDate) {
        this.type = type;
        this.acctNum = acctNum;
        this.status = status;
        this.balance = balance;
        this.paymentDate = paymentDate;
        paymentAmt = -1;
    }

    public Account(String type, int acctNum, String status, double balance, LocalDate paymentDate, double paymentAmt) {
        this(type, acctNum, status, balance, paymentDate);
        this.paymentAmt = paymentAmt;
    }

    public String getType() {
        return type;
    }

    public int getAcctNum() {
        return acctNum;
    }

    public String getStatus() {
        return status;
    }

    public double getBalance() {
        return balance;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public double getPaymentAmt() {
        return paymentAmt;
    }

    public void setPaymentAmt(double paymentAmt) {
        this.paymentAmt = paymentAmt;
    }
}
