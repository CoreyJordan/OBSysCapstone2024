package org.obsys.obsysapp.controllers;

import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.obsys.obsysapp.data.AccountDAO;
import org.obsys.obsysapp.data.ObsysDbConnection;
import org.obsys.obsysapp.data.PersonDAO;
import org.obsys.obsysapp.data.TransactionDAO;
import org.obsys.obsysapp.domain.Account;
import org.obsys.obsysapp.domain.Login;
import org.obsys.obsysapp.domain.MonthlySummary;
import org.obsys.obsysapp.domain.Person;
import org.obsys.obsysapp.models.AccountModel;
import org.obsys.obsysapp.models.StatementModel;
import org.obsys.obsysapp.views.AccountView;
import org.obsys.obsysapp.views.ViewBuilder;

import java.sql.Connection;
import java.sql.SQLException;

public class AccountController {
    private final Stage stage;
    private final ViewBuilder viewBuilder;
    private AccountModel acctModel;
    private Login login;

    public AccountController(Stage stage, AccountModel acctModel, Login login) {
        this.stage = stage;
        this.acctModel = acctModel;
        viewBuilder = new AccountView(acctModel, this::goHome, this::logout,
                this::goToTransactions, this::goToStatement);
        this.login = login;
    }

    private void goToStatement() {
        System.out.println(acctModel.getSelectedMonth());

        // TODO Create monthly summary, person, account
        // TODO Create Statement Model

        // TODO Navigate to statement page

        try (Connection conn = ObsysDbConnection.openDBConn()) {
            StatementModel stmtModel = new StatementModel(
                    new PersonDAO().readPersonByPersonId(conn, login.getPersonId()),
                    new Account(acctModel.getType(), acctModel.getAcctNum(), acctModel.getStatus(),
                            acctModel.getBalance(), acctModel.getDateOpened(), acctModel.getInstallment(),
                            acctModel.getInterestRate(), acctModel.getTerm(), acctModel.getInterestPaid()),
                    new MonthlySummary(new TransactionDAO().readTransactionsByMonth(conn, acctModel.getSelectedMonth())),
                    acctModel.getSelectedMonth()
            );

            stage.setScene(new Scene(new StatementController(stage, stmtModel, acctModel).getView()));

        } catch (Exception e) {
            stage.setScene(new Scene(new ErrorController(stage, e, this.getView()).getView()));
            e.printStackTrace();
        }
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
