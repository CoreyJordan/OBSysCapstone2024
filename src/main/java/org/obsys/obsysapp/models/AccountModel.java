package org.obsys.obsysapp.models;

import javafx.beans.property.*;
import org.obsys.obsysapp.domain.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;

public class AccountModel {
    private IntegerProperty acctNum;
    private StringProperty type = new SimpleStringProperty("");
    private DoubleProperty balance = new SimpleDoubleProperty(-1);
    private LocalDate dateOpened = LocalDate.MIN;
    private StringProperty status = new SimpleStringProperty("");
    private DoubleProperty interestRate = new SimpleDoubleProperty(-1);
    private DoubleProperty loanAmt = new SimpleDoubleProperty(-1);
    private IntegerProperty term = new SimpleIntegerProperty(-1);
    private DoubleProperty interestPaid = new SimpleDoubleProperty(-1);
    private DoubleProperty installment = new SimpleDoubleProperty(-1);
    private ArrayList<Transaction> history = new ArrayList<>();


    public ArrayList<Transaction> getHistory() {
        return history;
    }

    public int getAcctNum() {
        return acctNum.get();
    }

    public IntegerProperty acctNumProperty() {
        return acctNum;
    }

    public double getBalance() {
        return balance.get();
    }

    public DoubleProperty balanceProperty() {
        return balance;
    }

    public LocalDate getDateOpened() {
        return dateOpened;
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public double getInterestRate() {
        return interestRate.get();
    }

    public DoubleProperty interestRateProperty() {
        return interestRate;
    }

    public double getLoanAmt() {
        return loanAmt.get();
    }

    public DoubleProperty loanAmtProperty() {
        return loanAmt;
    }

    public int getTerm() {
        return term.get();
    }

    public IntegerProperty termProperty() {
        return term;
    }

    public double getInterestPaid() {
        return interestPaid.get();
    }

    public DoubleProperty interestPaidProperty() {
        return interestPaid;
    }

    public double getInstallment() {
        return installment.get();
    }

    public DoubleProperty installmentProperty() {
        return installment;
    }

    public double getInterestDue() {
        return interestDue.get();
    }

    public DoubleProperty interestDueProperty() {
        return interestDue;
    }

    public void setInstallment(double installment) {
        this.installment.set(installment);
    }

    private DoubleProperty interestDue;

    public void setType(String type) {
        this.type.set(type);
    }

    public void setBalance(double balance) {
        this.balance.set(balance);
    }

    public void setDateOpened(LocalDate dateOpened) {
        this.dateOpened = dateOpened;
    }

    public void setStatus(String status) {
        this.status.set(status);
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

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public AccountModel(IntegerProperty acctNum, StringProperty type, DoubleProperty balance, LocalDate dateOpened,
                        StringProperty status, DoubleProperty interestRate) {
        this.acctNum = acctNum;
        this.type = type;
        this.balance = balance;
        this.dateOpened = dateOpened;
        this.status = status;
        this.interestRate = interestRate;
    }

    public AccountModel(int acctNum) {
        this.acctNum = new SimpleIntegerProperty(acctNum);
    }
}
