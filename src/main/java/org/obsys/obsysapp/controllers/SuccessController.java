package org.obsys.obsysapp.controllers;

import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.obsys.obsysapp.data.AccountDAO;
import org.obsys.obsysapp.data.ObsysDbConnection;
import org.obsys.obsysapp.data.TransactionDAO;
import org.obsys.obsysapp.domain.Login;
import org.obsys.obsysapp.models.AccountModel;
import org.obsys.obsysapp.models.AdminHomeModel;
import org.obsys.obsysapp.models.SuccessModel;
import org.obsys.obsysapp.views.SuccessView;
import org.obsys.obsysapp.views.ViewBuilder;

import java.sql.Connection;

public class SuccessController {
    private final Stage stage;
    private final ViewBuilder viewBuilder;
    private final SuccessModel successModel;
    private final Login login;
    private final boolean isAdmin;

    public SuccessController(Stage stage,
                             SuccessModel successModel,
                             Login login,
                             boolean isAdmin) {
        this.stage = stage;
        this.successModel = successModel;
        this.login = login;
        this.isAdmin = isAdmin;
        viewBuilder = new SuccessView(
                successModel,
                this::logout,
                this::goBack);
    }

    private void logout() {
        stage.setScene(new Scene(new LoginController(
                stage, "dolphinExit.png", "Thank you!").getView()));
    }

    private void goBack() {
        if (isAdmin) {
            returnToAdminHome();
        } else {
            returnToUserAccount();
        }

    }

    private void returnToAdminHome() {
        // Currently returns to a blank admin home page. Will have to reenter
        // user lookup.
        stage.setScene(new Scene(new AdminHomeController(
                stage, new AdminHomeModel(login.getUsername()), login
        ).getView()));
    }

    private void returnToUserAccount() {
        try (Connection conn = ObsysDbConnection.openDBConn()) {
            int acctNum = successModel.getAccount().getAcctNum();
            AccountDAO accountDao = new AccountDAO();
            TransactionDAO transactDao = new TransactionDAO();

            AccountModel accountModel = accountDao.readFullAccountDetails(
                    conn, acctNum);
            accountModel.setHistory(transactDao.readTransactionHistory(
                    conn, acctNum));

            AccountController acctCtrl = new AccountController(
                    stage,
                    accountModel,
                    login
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
