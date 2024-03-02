package org.obsys.obsysapp.domain;

import java.time.LocalDate;

public class Account {
    private String type;
    private int acctNum;
    private String status;
    private double balance;

    private LocalDate paymentDate;
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
        this.type = type;
        this.acctNum = acctNum;
        this.status = status;
        this.balance = balance;
        this.paymentDate = paymentDate;
        this.paymentAmt = paymentAmt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAcctNum() {
        return acctNum;
    }

    public void setAcctNum(int acctNum) {
        this.acctNum = acctNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getPaymentAmt() {
        return paymentAmt;
    }

    public void setPaymentAmt(double paymentAmt) {
        this.paymentAmt = paymentAmt;
    }
}
