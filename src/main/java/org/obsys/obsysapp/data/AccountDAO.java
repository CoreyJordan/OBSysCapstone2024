package org.obsys.obsysapp.data;

import org.obsys.obsysapp.domain.Account;
import org.obsys.obsysapp.models.AccountModel;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class AccountDAO {

    /**
     * Queries the DB for summary account information. Does not return loan
     * account specific details.
     *
     * @param conn     Stable connection to the database
     * @param personId The user id that serves as the foreign key
     * @return Account information; type, id, status, balance
     * @throws SQLException new exception
     */
    public ArrayList<Account> readAccountsByPersonId(
            Connection conn, int personId) throws SQLException {
        ArrayList<Account> accounts = new ArrayList<>();
        String acctType;
        int acctId;
        String acctStatus;
        double balance;
        LocalDate paymentDate;


        try (PreparedStatement statement = conn.prepareStatement("""
                SELECT AccountType, AccountId, AcctStatus, Balance,
                    DateOpened
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
                accounts.add(new Account(
                        acctType, acctId, acctStatus, balance, paymentDate));
            }
        }
        return accounts;
    }

    /**
     * Queries DB for the dollar amount of regular loan installments using the
     * account number as a foreign key.
     *
     * @param conn    connection to the database
     * @param acctNum the foreign key linking to the account table
     * @return a single decimal value
     * @throws SQLException possible database errors
     */
    public double readPaymentDateByAcctNum(
            Connection conn, int acctNum) throws SQLException {
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
     * Queries the database for full account details to be displayed.
     * Transaction history goes back 1 month.
     *
     * @param conn                connection to the database
     * @param targetAccountNumber the account number being queried
     * @return primary account details, loan details if applicable, and 1 month
     * of history
     * @throws SQLException possible errors reading from 3 separate tables
     */
    public AccountModel readFullAccountDetails(
            Connection conn, int targetAccountNumber) throws SQLException {
        AccountModel account = new AccountModel(targetAccountNumber);

        try (PreparedStatement statement = conn.prepareStatement("""
                SELECT AccountType, Balance, DateOpened, AcctStatus,
                    InterestRate, LoanAmt, Term, InterestPaid, PaymentAmt,
                    InterestBalance
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
                account.setDateOpened(
                        resultSet.getDate("DateOpened").toLocalDate());
                account.setStatus(resultSet.getString("AcctStatus"));
                account.setInterestRate(resultSet.getDouble("InterestRate"));
                account.setLoanAmt(resultSet.getDouble("LoanAmt"));
                account.setTerm(resultSet.getInt("Term"));
                account.setInterestRecieved(
                        resultSet.getDouble("InterestPaid"));
                account.setInstallment(resultSet.getDouble("PaymentAmt"));
                account.setInterestDue(resultSet.getDouble("InterestBalance"));
            }
        }

        return account;
    }

    /**
     * Updates the balance of a target account. Must be run in conjunction with
     * a transaction query in order to maintain data integrity.
     *
     * @param conn      stable database connection
     * @param amount    dollar amount by which the account balance will be
     *                  modified
     * @param accountId target account to be updated
     * @return number of rows affected
     * @throws SQLException possible database update failure
     */
    public int updateBalance(
            Connection conn, double amount, int accountId) throws SQLException {
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

    /**
     * Reads a single balance from an account in order to update balances from
     * accounts outside the model data sets.
     *
     * @param conn      stable database connection
     * @param accountId target account
     * @return current dollar amount balance. Will return -9999999 if no balance
     * found.
     * @throws SQLException possible database update failure
     */
    public double readAcctBalanceById(
            Connection conn, int accountId) throws SQLException {
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

    /**
     * Updates the loan account interest due with the current payment
     * information.
     *
     * @param conn      stable database connection
     * @param amount    interest portion of the transaction
     * @param accountId loan account to which the payment is directed
     * @return number of rows affected
     * @throws SQLException possible database failures
     */
    public int updateInterestBalance(
            Connection conn, double amount, int accountId) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("""
                UPDATE dbo.Loan
                SET InterestBalance = InterestBalance - ?
                WHERE AccountId = ?
                    """)) {
            statement.setDouble(1, amount);
            statement.setInt(2, accountId);

            return statement.executeUpdate();
        }
    }

    /**
     * Queries the database for the personId number of an account  in order to
     * identify the account holder.
     *
     * @param conn       stable db connection
     * @param accountNum account number to be referenced
     * @return person id number
     * @throws SQLException possible db failures
     */
    public int readPersonIdByAccountNum(
            Connection conn, int accountNum) throws SQLException {
        int personId = 0;
        try (PreparedStatement statement = conn.prepareStatement("""
                SELECT PersonId
                FROM dbo.Account
                WHERE AccountId = ?
                    """)) {
            statement.setInt(1, accountNum);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                personId = resultSet.getInt(1);
            }
            return personId;
        }
    }

    /**
     * Creates a new account record in the database and returns the newly
     * created account number.
     * @param conn stable db connection
     * @param newAcct the account to be inserted
     * @return the inserted account number
     * @throws SQLException possible db failures
     */
    public int createAccount(
            Connection conn, Account newAcct) throws SQLException {
        int accountNum = 0;
        try (PreparedStatement statement = conn.prepareStatement("""
                INSERT INTO dbo.Account (PersonId, AccountType, Balance,
                    DateOpened, AcctStatus, InterestRate)
                VALUES (?, ?, ?, ?, ?, ?);
                SELECT IDENT_CURRENT('Account') AS AccountId;
                    """)) {
            statement.setInt(1, newAcct.getPersonId());
            statement.setString(2, newAcct.getType());
            statement.setDouble(3, newAcct.getBalance());
            statement.setDate(4, Date.valueOf(newAcct.getPaymentDate()));
            statement.setString(5, newAcct.getStatus());
            statement.setDouble(6, newAcct.getIntRate());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                accountNum = resultSet.getInt("AccountId");
            }
            return accountNum;
        }
    }

    /**
     * Appends the loan details of a newly created loan account to the loan
     * table of the database. Sets paid balances to 0.
     * @param conn stable db connection
     * @param newAcct the account to be inserted
     * @return 1 if successfully inserted, 0 if failed
     * @throws SQLException possible db failures
     */
    public int createLoanAccount(
            Connection conn, Account newAcct) throws SQLException{
        try (PreparedStatement statement = conn.prepareStatement("""
                INSERT INTO dbo.Loan (AccountId, LoanAmt, Term, PaymentAmt,
                    InterestPaid, InterestBalance)
                VALUES (?, ?, ?, ?, 0, 0)
                    """)) {
            statement.setInt(1, newAcct.getAcctNum());
            statement.setDouble(2, newAcct.getBalance());
            statement.setInt(3, newAcct.getTerm());
            statement.setDouble(4, newAcct.getPaymentAmt());

            return statement.executeUpdate();
        }
    }
}
