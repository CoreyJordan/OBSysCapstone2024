package org.obsys.obsysapp.domain;

public class Payee {
    private final int payeeNumber;
    private final String description;

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
