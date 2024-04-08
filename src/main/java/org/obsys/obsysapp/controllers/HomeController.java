package org.obsys.obsysapp.controllers;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Builder;
import org.obsys.obsysapp.data.AccountDAO;
import org.obsys.obsysapp.data.ObsysDbConnection;
import org.obsys.obsysapp.data.TransactionDAO;
import org.obsys.obsysapp.domain.Account;
import org.obsys.obsysapp.domain.Login;
import org.obsys.obsysapp.models.AccountModel;
import org.obsys.obsysapp.models.AccountsModel;
import org.obsys.obsysapp.views.HomeView;

import java.sql.Connection;
import java.util.ArrayList;

public class HomeController {
    private final Stage stage;
    private final AccountDAO acctDao;
    private final TransactionDAO transactDao;
    private final Login user;
    private Builder<AnchorPane> viewBuilder;
    private AccountsModel accountsModel;

    public HomeController(Stage stage, Login user) {
        this.user = user;
        this.stage = stage;
        acctDao = new AccountDAO();
        transactDao = new TransactionDAO();
        ArrayList<Account> accounts = new ArrayList<>();

        try (Connection conn = ObsysDbConnection.openDBConn()) {
            accounts = acctDao.readAccountsByPersonId(conn, user.getPersonId());
            for (Account acct : accounts) {
                if (acct.getType().equals("LN")) {
                    acct.setPaymentAmt(acctDao.readPaymentDateByAcctNum(
                            conn, acct.getAcctNum()));
                }
            }
        } catch (Exception e) {
            ErrorController eCtrl = new ErrorController(stage, e.getMessage());
            stage.setScene(new Scene(eCtrl.getView()));
        }

        accountsModel = new AccountsModel(accounts);
        viewBuilder = new HomeView(
                user,
                accountsModel,
                this::logout,
                this::navigate);
    }

    private void logout() {
        // Clear working data for security
        accountsModel = null;
        viewBuilder = null;

        stage.setScene(new Scene(new LoginController(
                stage, "dolphinExit.png", "Thank you!").getView()));
    }

    private void navigate() {
        try (Connection conn = ObsysDbConnection.openDBConn()) {
            int acctNum = accountsModel.getTargetAccountNumber();

            AccountModel accountModel = acctDao.readFullAccountDetails(
                    conn, acctNum);
            accountModel.setHistory(transactDao.readTransactionHistory(
                    conn, acctNum));

            AccountController acctCtrl = new AccountController(
                    stage,
                    accountModel,
                    user
            );
            stage.setScene(new Scene(acctCtrl.getView()));
        } catch (Exception e) {
            ErrorController eCtrl = new ErrorController(
                    stage,
                    e.getMessage(),
                    this.getView()
            );
            stage.setScene(new Scene(eCtrl.getView()));
        }

    }

    public Region getView() {
        return viewBuilder.build();
    }
}
