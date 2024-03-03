package org.obsys.obsysapp.data;

import org.obsys.obsysapp.domain.Account;
import org.obsys.obsysapp.models.AccountModel;

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
        String acctType;
        int acctId;
        String acctStatus;
        double balance;
        LocalDate paymentDate;


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

    /**
     * Queries DB for the dollar amount of regular loan installments using the account number as a foreign key.
     * @param conn connection to the database
     * @param acctNum the foreign key linking to the account table
     * @return a single decimal value
     * @throws SQLException possible database errors
     */
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

    /**
     * Queries the database for full account details to be displayed. Transaction history goes back 1 month.
     * @param conn connection to the database
     * @param targetAccountNumber the account number being queried
     * @return primary account details, loan details if applicable, and 1 month of history
     * @throws SQLException possible errors reading from 3 separate tables
     */
    public AccountModel readFullAccountDetails(Connection conn, int targetAccountNumber) throws SQLException {
        AccountModel account = new AccountModel(targetAccountNumber);

        readPrimaryDetails(conn, account, targetAccountNumber);
//        if (account.getType().equals("LN")) {
//            readLoanDetails(conn, account, targetAccountNumber);
//        }
//        readTransactions(conn, account, targetAccountNumber);
        
        return account;
    }

    private void readTransactions(Connection conn, AccountModel account, int acctNum) throws SQLException {
        // TODO query for transactions
    }

    private void readLoanDetails(Connection conn, AccountModel account, int acctNum) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("""
                SELECT LoanAmt, Term, InterestPaid, PaymentAmt, InterestBalance
                FROM dbo.Loan
                WHERE AccountId = ?;
                """)) {
            statement.setInt(1, acctNum);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                account.setLoanAmt(resultSet.getDouble(1));
                account.setTerm(resultSet.getInt(2));
                account.setInterestPaid(resultSet.getDouble(3));
                account.setInstallment(resultSet.getDouble(4));
                account.setInterestDue(resultSet.getDouble(5));
            }

        } catch (SQLException e) {
            throw new SQLException("Problem reading loan details");
        }
    }

    private void readPrimaryDetails(Connection conn, AccountModel account, int acctNum) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("""
                    SELECT AccountType, Balance, DateOpened, AcctStatus, InterestRate
                    FROM dbo.Account
                    WHERE AccountId = ?;
                    """)) {
            statement.setInt(1, acctNum);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                account.setType(resultSet.getString(1));
                account.setBalance(resultSet.getDouble(2));
                account.setDateOpened(resultSet.getDate(3).toLocalDate());
                account.setStatus(resultSet.getString(4));
                account.setInterestRate(resultSet.getDouble(5));
            }

        } catch (SQLException e) {
            throw new SQLException("Problem reading account details");
        }
    }
}
