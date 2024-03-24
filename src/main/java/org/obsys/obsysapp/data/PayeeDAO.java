package org.obsys.obsysapp.data;

import org.obsys.obsysapp.domain.Payee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PayeeDAO {
    /**
     * Reads the database and generates a list of all payees previously associated with transactions pertaining to the
     * target account. Does not return payees that may be associated with the user through other accounts they hold.
     * Used primarily to populate combo boxes in the transaction pages.
     *
     * @param conn       stable connection to the Obsys DB
     * @param accountNum account associated with the account activity
     * @return ArrayList of payees
     * @throws SQLException possible database failures
     */
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
                        resultSet.getInt("PayeeId"),
                        resultSet.getString("PayeeDescription")));
            }
        }
        return payees;
    }

    /**
     * Reads basic account information from the database and creates a list of found accounts. Used to populate
     * combo boxes in the transfer/loan screens. Excludes the currently selected account. Does not return loan accounts
     * as they cannot be transacted.
     *
     * @param conn       stable connection to the Obsys DB
     * @param personId   owner of accounts list
     * @param accountNum account being excluded
     * @return ArrayList of basic account information
     * @throws SQLException possible database failures
     */
    public ArrayList<Payee> readAccountsByPersonId(Connection conn, int personId, int accountNum) throws SQLException {
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

    /**
     * Inserts a new payee into the database. Will first verify the payee does not already exist. The database does not
     * enforce uniqueness on description, however this initial search will prevent identical, but not similar insertions.
     *
     * @param conn             stable connection to the Obsys DB
     * @param transactionPayee payee description to be inserted
     * @throws SQLException possible database failures
     */
    public void insertNewPayee(Connection conn, String transactionPayee) throws SQLException {
        if (readPayeeIdByDescription(conn, transactionPayee) != 0) {
            return;
        }

        try (PreparedStatement statement = conn.prepareStatement("""
                INSERT INTO dbo.Payee
                VALUES(?)
                    """)) {
            statement.setString(1, transactionPayee);

            statement.executeUpdate();
        }
    }

    /**
     * Searches the database for a matching payee description. The database does not enforce uniqueness on descriptions
     * but this query will only return 1 identical result. Will return 0 if not found.
     *
     * @param conn             stable connection to the Obsys DB
     * @param transactionPayee payee description to be searched
     * @return the payeeId corresponding to a found description, 0 if not found
     * @throws SQLException possible database failures
     */
    public int readPayeeIdByDescription(Connection conn, String transactionPayee) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("""
                SELECT PayeeId
                FROM dbo.Payee
                WHERE PayeeDescription = ?
                    """)) {
            statement.setString(1, transactionPayee);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("PayeeId");
            }
        }
        return 0;
    }
}
