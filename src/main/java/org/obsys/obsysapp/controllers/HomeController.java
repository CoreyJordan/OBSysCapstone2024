package org.obsys.obsysapp.controllers;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Builder;
import org.obsys.obsysapp.data.AccountDAO;
import org.obsys.obsysapp.data.ObsysDbConnection;
import org.obsys.obsysapp.domain.Account;
import org.obsys.obsysapp.domain.Login;
import org.obsys.obsysapp.models.AccountsModel;
import org.obsys.obsysapp.views.HomeView;

import java.sql.Connection;
import java.util.ArrayList;

public class HomeController {
    private final Stage stage;
    private final AccountDAO acctDao;
    private Builder<AnchorPane> viewBuilder;
    private AccountsModel acctsModel;

    public HomeController(Stage stage, Login user) {

        this.stage = stage;
        acctDao = new AccountDAO();
        ArrayList<Account> accounts = new ArrayList<>();

        try (Connection conn = ObsysDbConnection.openDBConn()) {
            accounts = acctDao.readAccountsByPersonId(conn, user.getPersonId());
            for (Account acct : accounts) {
                if (acct.getType().equals("LN")) {
                    acct.setPaymentAmt(acctDao.readPaymentDateByAcctNum(conn, acct.getAcctNum()));
                }
            }
        } catch (Exception e) {
            stage.setScene(new Scene(new ErrorController(stage, e).getView()));
        }

        acctsModel = new AccountsModel(accounts);
        viewBuilder = new HomeView(user, acctsModel, this::logout, this::navigate);
    }

    private void logout() {
        // Clear working data for security
        acctsModel = null;
        viewBuilder = null;

        stage.setScene(new Scene(new LoginController(
                stage, "dolphinExit.png", "Thank you!").getView()));
    }

    private void navigate() {
        // TODO navigate to pages
    }

    public Region getView() {
        return viewBuilder.build();
    }
}
