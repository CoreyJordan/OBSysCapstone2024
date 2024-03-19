package org.obsys.obsysapp.testing;

import org.obsys.obsysapp.domain.Account;

import java.time.LocalDate;
import java.util.ArrayList;

public class AccountsSample {
    ArrayList<Account> sampleAccounts = new ArrayList<>();

    public AccountsSample() {
        LocalDate date = LocalDate.of(2024, 1, 23);
        this.sampleAccounts.add(new Account("CH", 1111111111, "OP", 1241.53, date));
        this.sampleAccounts.add(new Account("SV", 1111111112, "DQ", 1076.70, date));
        this.sampleAccounts.add(new Account("IC", 1111111113, "CL", 323.93, date));
        this.sampleAccounts.add(new Account("LN", 1111111114, "SU", 17055.26, date, 300.12));
//        this.sampleAccounts.add(new Account("CH", 1111111111, "OP", 1241.53));
//        this.sampleAccounts.add(new Account("SV", 1111111112, "OP", 1076.70));
//        this.sampleAccounts.add(new Account("IC", 1111111113, "OP", 323.93));
//        this.sampleAccounts.add(new Account("LN", 1111111114, "OP", 17055.26, date, 400.10));
    }

    public ArrayList<Account> getSampleAccounts() {
        return sampleAccounts;
    }
}
