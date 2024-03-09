package org.obsys.obsysapp.data;

import org.obsys.obsysapp.domain.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TransactionDAO {
    public ArrayList<Transaction> readTransactionHistory(Connection conn, int acctNum) throws SQLException {
        ArrayList<Transaction> history = new ArrayList<>();

        try (PreparedStatement statement = conn.prepareStatement("""
                SELECT * FROM dbo.AccountActivity
                LEFT OUTER JOIN dbo.Payee
                ON dbo.Payee.PayeeId = dbo.AccountActivity.Payee
                WHERE AccountId = ?
                ORDER BY TransactionDate DESC ;
                """)) {
            statement.setInt(1, acctNum);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                history.add(new Transaction(
                        resultSet.getString("TransactionType"),
                        resultSet.getDouble("TransactionAmt"),
                        resultSet.getDate("TransactionDate").toLocalDate(),
                        resultSet.getInt("TransferTo"),
                        resultSet.getString("PayeeDescription"),
                        resultSet.getDouble("ToPrincipal"),
                        resultSet.getDouble("ToInterest")
                ));
            }
        }


        return history;
    }

}
