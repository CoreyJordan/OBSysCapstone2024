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

        try (PreparedStatement statement = conn.prepareStatement("""
                SELECT AccountType, Balance, DateOpened, AcctStatus, InterestRate, LoanAmt, Term, InterestPaid,
                    PaymentAmt, InterestBalance
                FROM dbo.Account
                LEFT OUTER JOIN dbo.Loan
                ON Account.AccountId = Loan.AccountId
                WHERE dbo.Account.AccountId = ?;
                """)) {
            statement.setInt(1, targetAccountNumber);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                account.setType(resultSet.getString("AccountType"));
                account.setBalance(resultSet.getDouble("Balance"));
                account.setDateOpened(resultSet.getDate("DateOpened").toLocalDate());
                account.setStatus(resultSet.getString("AcctStatus"));
                account.setInterestRate(resultSet.getDouble("InterestRate"));
                account.setLoanAmt(resultSet.getDouble("LoanAmt"));
                account.setTerm(resultSet.getInt("Term"));
                account.setInterestPaid(resultSet.getDouble("InterestPaid"));
                account.setInstallment(resultSet.getDouble("PaymentAmt"));
                account.setInterestDue(resultSet.getDouble("InterestBalance"));
            }
        }
        
        return account;
    }

    public int updateBalance(Connection conn, double amount, int accountId) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("""
                UPDATE dbo.Account
                SET Balance = Balance + ?
                WHERE AccountId = ?
                """)) {
            statement.setDouble(1, amount);
            statement.setInt(2, accountId);

            return statement.executeUpdate();
        }
    }

    public double readAcctBalanceById(Connection conn, int accountId) throws SQLException {
        double balance = -99999999;
        try (PreparedStatement statement = conn.prepareStatement("""
                SELECT Balance
                FROM dbo.Account
                WHERE AccountId = ?
                    """)) {
            statement.setInt(1, accountId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                balance = resultSet.getDouble("Balance");
            }
        }
        return balance;
    }
}
