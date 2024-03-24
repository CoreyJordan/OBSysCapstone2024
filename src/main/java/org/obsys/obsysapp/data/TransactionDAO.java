package org.obsys.obsysapp.data;

import org.obsys.obsysapp.domain.Transaction;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class TransactionDAO {
    /**
     * Returns the complete transaction history of an account. For older accounts, this may return excessive results.
     * Payees are converted into descriptions for meaningful user data. Transfer/Payment accounts will need to be
     * formatted at the class level.
     *
     * @param conn    stable connection to the Obsys DB
     * @param acctNum target account
     * @return all transactions associated with the account
     * @throws SQLException possible database failure
     */
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

    /**
     * Returns a single month transaction history of an account. Payees are converted into descriptions for meaningful
     * user data. Transfer/Payment accounts will need to be formatted at the class level.
     *
     * @param conn    stable connection to the Obsys DB
     * @param date    target month
     * @param acctNum target account
     * @return all transactions associated with the account
     * @throws SQLException possible database failure
     */
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

    /**
     * Inserts a deposit or withdrawal transaction into the DB. Needs to be run alongside an account balance update.
     *
     * @param conn        stable connection to the Obsys DB
     * @param transaction deposit/withdrawal
     * @param balance     current dollar balance this transaction will update
     * @return number of rows affected
     * @throws SQLException possible database failure
     */
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

    /**
     * Inserts a transfer transaction into the DB. Two transfers queries are to be run in conjunction. One to deposit
     * into the target account and one to withdraw from the source account. Needs to be run alongside an account balance update.
     *
     * @param conn        stable connection to the Obsys DB
     * @param transaction transfer deposit / transfer withdrawal
     * @param balance     current dollar balance this transaction will update
     * @return number of rows affected
     * @throws SQLException possible database failure
     */
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
