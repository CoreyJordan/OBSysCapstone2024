package org.obsys.obsysapp.domain;

import java.util.ArrayList;
import java.util.Comparator;

public class MonthlySummary {
    private final double balanceBegin;
    private final double balanceEnd;
    private final double credits;
    private final double debits;

    private final ArrayList<Transaction> transactions;

    // Checking Constructor
    public MonthlySummary(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
        this.transactions.sort(Comparator.comparing(Transaction::getDate));
        double balance = this.transactions.getFirst().getBalance();
        balanceBegin = balance - this.transactions.getFirst().getAmount();
        balanceEnd = this.transactions.getLast().getBalance();
        credits = calculateCredits(this.transactions);
        debits = calculateDebits(this.transactions);
    }

    // GETTERS
    public double getBalanceBegin() {
        return balanceBegin;
    }

    public double getBalanceEnd() {
        return balanceEnd;
    }

    public double getCredits() {
        return credits;
    }

    public double getDebits() {
        return debits;
    }

    public double getInterestPaid() {
        return 0;
    }

    public double getFees() {
        // No fees in transactions at this time
        return 0;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    private double calculateCredits(ArrayList<Transaction> transactions) {
        double total = 0;
        for (Transaction t : transactions) {
            if (t.getType().equals("DP")) {
                total += t.getAmount();
            }
        }
        return total;
    }

    private double calculateDebits(ArrayList<Transaction> transactions) {
        double total = 0;
        for (Transaction t : transactions) {
            if (!t.getType().equals("DP")) {
                total += t.getAmount();
            }
        }
        return total;
    }
}
