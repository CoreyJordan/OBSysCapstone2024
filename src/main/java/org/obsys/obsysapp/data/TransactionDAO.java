package org.obsys.obsysapp.data;

import org.obsys.obsysapp.domain.Transaction;

import java.sql.*;
import java.time.LocalDate;
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

    public ArrayList<Transaction> readTransactionsByMonth(Connection conn, LocalDate date, int acctNum) throws SQLException {
        ArrayList<Transaction> transactions = new ArrayList<>();

        try (PreparedStatement statement = conn.prepareStatement("""
                SELECT * FROM dbo.AccountActivity
                LEFT OUTER JOIN dbo.Payee
                ON dbo.Payee.PayeeId = dbo.AccountActivity.Payee
                WHERE TransactionDate BETWEEN ? AND ?
                AND AccountId = ?;
                """)) {
            statement.setDate(1, Date.valueOf(date.minusMonths(1).plusDays(1)));
            statement.setDate(2, Date.valueOf(date));
            statement.setInt(3, acctNum);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                transactions.add(new Transaction(
                        resultSet.getString("TransactionType"),
                        resultSet.getDouble("TransactionAmt"),
                        resultSet.getDate("TransactionDate").toLocalDate(),
                        resultSet.getInt("TransferTo"),
                        resultSet.getString("PayeeDescription"),
                        resultSet.getDouble("ToPrincipal"),
                        resultSet.getDouble("ToInterest"),
                        resultSet.getDouble("Balance")
                ));
            }
        }

        return transactions;
    }

    public int insertTransaction(Connection conn, Transaction transaction, double balance) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("""
            INSERT INTO AccountActivity (TransactionType, TransactionAmt, TransactionDate, AccountId, Payee, Balance)
            VALUES(?,?,?,?,?,?);
                """)) {
            statement.setString(1, transaction.getType());
            statement.setDouble(2, transaction.getAmount());
            statement.setDate(3, Date.valueOf(transaction.getDate()));
            statement.setInt(4, transaction.getAccountId());
            statement.setInt(5, transaction.getPayeeId());
            statement.setDouble(6, balance + transaction.getAmount());

            return statement.executeUpdate();
        }
    }

    public int insertTransfer(Connection conn, Transaction transaction, double balance) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("""
            INSERT INTO AccountActivity (TransactionType, TransactionAmt, TransactionDate, AccountId, TransferTo, Balance)
            VALUES(?,?,?,?,?,?);
                """)) {
            statement.setString(1, transaction.getType());
            statement.setDouble(2, transaction.getAmount());
            statement.setDate(3, Date.valueOf(transaction.getDate()));
            statement.setInt(4, transaction.getAccountId());
            statement.setInt(5, transaction.getTransferToAcctId());
            statement.setDouble(6, balance + transaction.getAmount());

            return statement.executeUpdate();
        }
    }
}
