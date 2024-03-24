package org.obsys.obsysapp.data;

import org.obsys.obsysapp.domain.Payee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PayeeDAO {
    public ArrayList<Payee> readPayeesByAccount(Connection conn, int accountNum) throws SQLException {
        ArrayList<Payee> payees = new ArrayList<>();

        try (PreparedStatement statement = conn.prepareStatement("""
                SELECT DISTINCT PayeeId, PayeeDescription
                FROM dbo.Payee P
                JOIN dbo.AccountActivity AA
                ON P.PayeeId = AA.Payee
                WHERE AA.AccountId = ?
                """)) {
            statement.setInt(1, accountNum);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                payees.add(new Payee(
                        resultSet.getInt(  "PayeeId"),
                        resultSet.getString("PayeeDescription")));
            }
        }
        return payees;
    }

    public ArrayList<Payee> readAccoutsByPersonId(Connection conn, int personId, int accountNum) throws SQLException {
        ArrayList<Payee> payees = new ArrayList<>();

        try (PreparedStatement statement = conn.prepareStatement("""
                SELECT AccountId, AccountDescription, Balance
                FROM Account A
                JOIN dbo.AccountType T on T.AcctTypeId = A.AccountType
                WHERE PersonId = ?
                AND NOT AccountId = ?
                AND NOT AccountType = 'LN'
                """)) {
            statement.setInt(1, personId);
            statement.setInt(2, accountNum);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                payees.add(new Payee(
                        resultSet.getInt("AccountId"),
                        resultSet.getString("AccountDescription") + " ..." +
                                String.valueOf(resultSet.getInt("AccountId")).substring(6) +
                                String.format(" $%,.2f", resultSet.getDouble("Balance")
                )));
            }
        }
        return payees;
    }

    public void insertNewPayee(Connection conn, String transactionPayee) {
        // TODO insert new payee
    }

    public int readPayeeIdByDescription(Connection conn, String transactionPayee) {
        // TODO read payee ID
        return -1;
    }
}
