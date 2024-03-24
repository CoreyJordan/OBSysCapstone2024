package org.obsys.obsysapp.domain;

public class Payee {
    private int payeeNumber;
    private String description;

    public Payee(int payeeNumber, String description) {
        this.payeeNumber = payeeNumber;
        this.description = description;
    }

    public int getPayeeNumber() {
        return payeeNumber;
    }

    public String getDescription() {
        return description;
    }
}
