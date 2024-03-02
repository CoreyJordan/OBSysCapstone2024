package org.obsys.obsysapp.data;

import org.obsys.obsysapp.domain.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class AccountDAO {


    /**
     * Queries the DB for summary account information. Does not return loan account specific details.
     * @param conn Stable connection to the database
     * @param personId The user id that serves as the foreign key
     * @return Account information; type, id, status, balance
     * @throws SQLException new exception
     */
    public ArrayList<Account> readAccountsByPersonId(Connection conn, int personId) throws SQLException {
        ArrayList<Account> accounts = new ArrayList<>();
        String acctType = null;
        int acctId = 0;
        String acctStatus = null;
        double balance = 0;
        LocalDate paymentDate = null;


        try (PreparedStatement statement = conn.prepareStatement("""
                    SELECT AccountType, AccountId, AcctStatus, Balance, DateOpened
                    FROM dbo.Account
                    WHERE PersonId = ?;
                    """)) {
            statement.setInt(1, personId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                acctType = resultSet.getString(1);
                acctId = resultSet.getInt(2);
                acctStatus = resultSet.getString(3);
                balance = resultSet.getDouble(4);
                paymentDate = resultSet.getDate(5).toLocalDate();
                accounts.add(new Account(acctType, acctId, acctStatus, balance, paymentDate));
            }
        }
        return accounts;
    }

    public double readPaymentDateByAcctNum(Connection conn, int acctNum) throws SQLException{
        double paymentAmt = -1;
        try (PreparedStatement statement = conn.prepareStatement("""
                SELECT PaymentAmt
                FROM dbo.Loan
                WHERE AccountId = ?;
                """)) {
            statement.setInt(1, acctNum);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                paymentAmt = resultSet.getDouble(1);
            }
        }
        return paymentAmt;
    }
}
