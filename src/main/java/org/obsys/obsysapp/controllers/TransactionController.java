package org.obsys.obsysapp.controllers;

import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.obsys.obsysapp.data.AccountDAO;
import org.obsys.obsysapp.data.ObsysDbConnection;
import org.obsys.obsysapp.data.PayeeDAO;
import org.obsys.obsysapp.data.TransactionDAO;
import org.obsys.obsysapp.domain.Payee;
import org.obsys.obsysapp.domain.Transaction;
import org.obsys.obsysapp.models.SuccessModel;
import org.obsys.obsysapp.models.TransactionModel;
import org.obsys.obsysapp.views.TransactionView;
import org.obsys.obsysapp.views.ViewBuilder;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionController {
    private final Stage stage;
    private final ViewBuilder view;
    private final TransactionModel transactionModel;
    private final Region accountView;
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final AccountDAO accountDao = new AccountDAO();
    private final PayeeDAO payeeDao = new PayeeDAO();

    public TransactionController(Stage stage, Region accountView, TransactionModel transactionModel) {
        this.stage = stage;
        this.transactionModel = transactionModel;
        this.accountView = accountView;
        view = new TransactionView(transactionModel, this::logout, this::goBack, this::transact);
    }

    public Region getView() {
        return view.build();
    }

    private void logout() {

        stage.setScene(new Scene(new LoginController(
                stage, "dolphinExit.png", "Thank you!").getView()));
    }

    private void goBack() {
        stage.setScene(new Scene(accountView));
    }

    private void transact() {
        clearErrors();

        if (transactionModel.getTransactionAmount().isEmpty()) {
            transactionModel.setAmountError("Enter a dollar amount");
            return;
        }
        double amount = Double.parseDouble(transactionModel.getTransactionAmount());
        if (amount <= 0) {
            transactionModel.setAmountError("Amount must be greater than 0");
            return;
        }

        // Create objects for standard deposits and withdrawals. Transfers and payments will create their own objects.
        Transaction transaction = new Transaction(transactionModel.getTransactionType(), amount,
                transactionModel.getTransactionDate(), transactionModel.getTransactionPayee());
        double accountBalance = transactionModel.getAccount().getBalance();
        transaction.setAccountId(transactionModel.getAccount().getAcctNum());

        try (Connection conn = ObsysDbConnection.openDBConn()) {
            switch (transactionModel.getTransactionType()) {
                case "WD" -> {
                    setPayeeId(transaction, conn);
                    withdrawFunds(conn, transaction, accountBalance);
                }
                case "TF" -> transferFunds(conn, transaction);
                case "PY" -> makePayment(amount);
                default -> {
                    setPayeeId(transaction, conn);
                    depositFunds(conn, transaction, accountBalance);
                }
            }
            transaction.setReferenceId(transactionDAO.readLastTransactionId(conn));
            System.out.println(transaction.getReferenceId());
            stage.setScene(
                    new Scene(
                            new SuccessController(stage,
                                    new SuccessModel(transaction, transactionModel.getAccount())).getView()));
        } catch (Exception e) {
            stage.setScene(new Scene(new ErrorController(stage, e.getMessage(), this.getView()).getView()));
        }
    }

    private void setPayeeId(Transaction transaction, Connection conn) throws SQLException {
        if (matchPayeeId() != 0) {
            transaction.setPayeeId(matchPayeeId());
        } else {
            payeeDao.insertNewPayee(conn, transactionModel.getTransactionPayee());
            transaction.setPayeeId(payeeDao.readPayeeIdByDescription(conn, transactionModel.getTransactionPayee()));
        }
    }

    private void depositFunds(Connection conn, Transaction deposit, double acctBalance) throws SQLException {
        if (transactionModel.getTransactionPayee().isEmpty()) {
            transactionModel.setPayeeError("Select payee or insert new payee");
            return;
        }

        deposit.setBalanceResult(acctBalance + deposit.getAmount());
        if (transactionDAO.insertTransaction(conn, deposit) != 1 ||
                accountDao.updateBalance(conn, deposit.getAmount(), deposit.getAccountId()) != 1) {
            String depositFailed = "Unable to deposit funds. Please contact your bank representative to resolve.";
            throw new SQLException(depositFailed);
        }
    }

    private void makePayment(double amount) {
        if (transactionModel.getTransactionPayee().isEmpty()) {
            transactionModel.setPayeeError("Select a source account");
            return;
        }

        // We will let the user make this transaction but warn them
        double due = transactionModel.getAccount().getPaymentAmt();
        if (amount < due) {
            transactionModel.setAmountError("Payment is less than amount due");
        }

    }

    private void transferFunds(Connection conn, Transaction withdrawTransfer) throws SQLException {
        if (transactionModel.getTransactionPayee().isEmpty()) {
            transactionModel.setPayeeError("Select an account to transfer from");
            return;
        }

        double withdrawAcctBalance = transactionModel.getAccount().getBalance();

        if (withdrawTransfer.getAmount() > withdrawAcctBalance) {
            transactionModel.setAmountError("Insufficient funds");
            return;
        }

        withdrawTransfer = new Transaction(
                withdrawTransfer.getType(), withdrawTransfer.getAmount() * -1, withdrawTransfer.getDate(),
                transactionModel.getAccount().getAcctNum(), matchPayeeId());
        withdrawTransfer.setBalanceResult(withdrawAcctBalance + withdrawTransfer.getAmount());
        Transaction depositTransfer = new Transaction(
                withdrawTransfer.getType(), withdrawTransfer.getAmount(), withdrawTransfer.getDate(),
                matchPayeeId(), transactionModel.getAccount().getAcctNum());
        double depositAcctBalance = accountDao.readAcctBalanceById(conn, depositTransfer.getAccountId());
        depositTransfer.setBalanceResult(depositAcctBalance + depositTransfer.getAmount());


        // Perform the deposit side first in order to ensure that the last transaction performed is the withdrawal side
        // in order to retrieve the correct reference ID
        if (transactionDAO.insertTransfer(conn, depositTransfer) != 1 ||
                accountDao.updateBalance(conn, depositTransfer.getAmount(), depositTransfer.getAccountId()) != 1 ||
                transactionDAO.insertTransfer(conn, withdrawTransfer) != 1 ||
                accountDao.updateBalance(conn, withdrawTransfer.getAmount(), withdrawTransfer.getAccountId()) != 1) {
            String transferFailed = "Unable to transfer funds. Please contact your bank representative to resolve.";
            throw new SQLException(transferFailed);
        }
    }

    private void withdrawFunds(Connection conn, Transaction withdrawal, double acctBalance) throws SQLException {
        if (transactionModel.getTransactionPayee().isEmpty()) {
            transactionModel.setPayeeError("Select payer or insert new payer");
            return;
        }

        if (withdrawal.getAmount() > acctBalance) {
            transactionModel.setAmountError("Insufficient funds");
            return;
        }

        withdrawal.setAmount(withdrawal.getAmount() * -1);
        withdrawal.setBalanceResult(acctBalance + withdrawal.getAmount());
        if (transactionDAO.insertTransaction(conn, withdrawal) != 1 ||
                accountDao.updateBalance(conn, withdrawal.getAmount(), withdrawal.getAccountId()) != 1) {
            String transferFailed = "Unable to transfer funds. Please contact your bank representative to resolve.";
            throw new SQLException(transferFailed);
        }
    }

    private void clearErrors() {
        transactionModel.setPayeeError("");
        transactionModel.setAmountError("");
    }

    private int matchPayeeId() {

        for (Payee p : transactionModel.getPayees()) {
            if (p.getDescription().equals(transactionModel.getTransactionPayee())) {
                return p.getPayeeNumber();
            }
        }
        return 0;
    }
}
