package org.obsys.obsysapp.utils;

public class AccountValidator extends ObsysValidator {
    private final String acctNum;
    private final String firstName;
    private final String lastName;

    public AccountValidator(String acctNum, String firstName, String lastName) {
        this.acctNum = acctNum;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public boolean okToSearch() {
        return acctNumIsValid() && nameIsValid(firstName) && nameIsValid(lastName);
    }

    public boolean nameIsValid(String name) {
        return !name.isEmpty() &&
                !containsNumeric(name) &&
                !containsSymbol(name);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean acctNumIsValid() {
        return acctNum.length() == 10 &&
                isInteger(acctNum);
    }
}
