package org.obsys.obsysapp.data;

import org.obsys.obsysapp.domain.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class TransactionDAO {
    public ArrayList<Transaction> readTransactionHistoryOneMonth(Connection conn, int acctNum) throws SQLException {
        ArrayList<Transaction> history = new ArrayList<>();

        try (PreparedStatement statement = conn.prepareStatement("""
                SELECT * FROM dbo.AccountActivity
                LEFT OUTER JOIN dbo.Payee
                ON dbo.Payee.PayeeId = dbo.AccountActivity.Payee
                WHERE AccountId = ?
                AND TransactionDate > ?
                ORDER BY TransactionDate DESC ;
                """)) {
            statement.setInt(1, acctNum);
            statement.setDate(2, java.sql.Date.valueOf(LocalDate.now().minusMonths(1)));

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
