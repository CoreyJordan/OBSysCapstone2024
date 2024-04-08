package org.obsys.obsysapp.data;

import org.obsys.obsysapp.domain.Transaction;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class TransactionDAO {
    /**
     * Returns the complete transaction history of an account. For older
     * accounts, this may return excessive results. Payees are converted into
     * descriptions for meaningful user data. Transfer/Payment accounts will
     * need to be formatted at the class level.
     *
     * @param conn    stable connection to the Obsys DB
     * @param acctNum target account
     * @return all transactions associated with the account
     * @throws SQLException possible database failure
     */
    public ArrayList<Transaction> readTransactionHistory(
            Connection conn, int acctNum) throws SQLException {
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
     * Returns a single month transaction history of an account. Payees are
     * converted into descriptions for meaningful user data. Transfer/Payment
     * accounts will need to be formatted at the class level.
     *
     * @param conn    stable connection to the Obsys DB
     * @param date    target month
     * @param acctNum target account
     * @return all transactions associated with the account
     * @throws SQLException possible database failure
     */
    public ArrayList<Transaction> readTransactionsByMonth(
            Connection conn, LocalDate date, int acctNum) throws SQLException {
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
     * Inserts a deposit or withdrawal transaction into the DB. Needs to be run
     * alongside an account balance update.
     *
     * @param conn        stable connection to the Obsys DB
     * @param transaction deposit/withdrawal
     * @return number of rows affected
     * @throws SQLException possible database failure
     */
    public int insertTransaction(
            Connection conn, Transaction transaction) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("""
                INSERT INTO AccountActivity (TransactionType, TransactionAmt,
                    TransactionDate, AccountId, Payee, Balance)
                VALUES(?,?,?,?,?,?);
                    """)) {
            statement.setString(1, transaction.getType());
            statement.setDouble(2, transaction.getAmount());
            statement.setDate(3, Date.valueOf(transaction.getDate()));
            statement.setInt(4, transaction.getAccountId());
            statement.setInt(5, transaction.getPayeeId());
            statement.setDouble(6, transaction.getBalance());

            return statement.executeUpdate();
        }
    }

    /**
     * Inserts a transfer transaction into the DB. Two transfers queries are to
     * be run in conjunction. One to deposit into the target account and one to
     * withdraw from the source account. Needs to be run alongside an account
     * balance update.
     *
     * @param conn        stable connection to the Obsys DB
     * @param transaction transfer deposit / transfer withdrawal
     * @return number of rows affected
     * @throws SQLException possible database failure
     */
    public int insertTransfer(
            Connection conn, Transaction transaction) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("""
                INSERT INTO AccountActivity (TransactionType, TransactionAmt,
                    TransactionDate, AccountId, TransferTo, Balance)
                VALUES(?,?,?,?,?,?);
                    """)) {
            statement.setString(1, transaction.getType());
            statement.setDouble(2, transaction.getAmount());
            statement.setDate(3, Date.valueOf(transaction.getDate()));
            statement.setInt(4, transaction.getAccountId());
            statement.setInt(5, transaction.getTransferToAcctId());
            statement.setDouble(6, transaction.getBalance());

            return statement.executeUpdate();
        }
    }

    /**
     * Returns the TransactionId of the last transaction inserted into the
     * database. This method MUST be called immediately after the transaction is
     *  inserted to prevent grabbing a later transaction.
     * @param conn stable Obsys DB connection
     * @return int TransactionId
     * @throws SQLException possible database failures
     */
    public int readLastTransactionId(Connection conn) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("""
                SELECT IDENT_CURRENT('AccountActivity') AS TransactionId
                    """)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("TransactionId");
            }
        }
        return 0;
    }

    public int insertPayment(
            Connection conn, Transaction payment) throws SQLException{
        try (PreparedStatement statement = conn.prepareStatement("""
                INSERT INTO AccountActivity (TransactionType, TransactionAmt,
                    TransactionDate, AccountId, TransferTo, ToPrincipal,
                    ToInterest, Balance)
                VALUES (?,?,?,?,?,?,?,?)
                    """)) {
            statement.setString(1, payment.getType());
            statement.setDouble(2, payment.getAmount());
            statement.setDate(3, Date.valueOf(payment.getDate()));
            statement.setInt(4, payment.getAccountId());
            statement.setInt(5, payment.getTransferToAcctId());
            statement.setDouble(6, payment.getAmtToPrincipal());
            statement.setDouble(7, payment.getAmtToInterest());
            statement.setDouble(8, payment.getBalance());

            return statement.executeUpdate();
        }
    }
}





