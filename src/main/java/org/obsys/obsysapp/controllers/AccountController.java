package org.obsys.obsysapp.controllers;

import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.obsys.obsysapp.data.AccountDAO;
import org.obsys.obsysapp.data.TransactionDAO;
import org.obsys.obsysapp.domain.Login;
import org.obsys.obsysapp.models.AccountModel;
import org.obsys.obsysapp.views.AccountView;
import org.obsys.obsysapp.views.ViewBuilder;

public class AccountController {
    private final Stage stage;
    private final ViewBuilder viewBuilder;
    private final AccountDAO acctDao;
    private TransactionDAO transactDao;
    private AccountModel acctModel;
    private Login login;

    public AccountController(Stage stage, AccountModel acctModel, Login login) {
        this.stage = stage;
        acctDao = new AccountDAO();
        this.acctModel = acctModel;
        viewBuilder = new AccountView(acctModel, this::goHome, this::logout,
                this::goToTransactions, this::goToStatement);
        this.login = login;
    }

    private void goToStatement() {
        // TODO Navigate to statements page
    }

    private void goToTransactions() {
        // TODO Navigate to the transactions page
    }

    private void goHome() {
        stage.setScene(new Scene(new HomeController(stage, login).getView()));
    }

    private void logout() {
        login = null;
        acctModel = null;

        stage.setScene(new Scene(new LoginController(
                stage, "dolphinExit.png", "Thank you!").getView()));
    }

    public Region getView() {
        return viewBuilder.build();
    }
}
