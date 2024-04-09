package org.obsys.obsysapp.controllers;

import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.obsys.obsysapp.data.ObsysDbConnection;
import org.obsys.obsysapp.data.PayeeDAO;
import org.obsys.obsysapp.data.PersonDAO;
import org.obsys.obsysapp.data.TransactionDAO;
import org.obsys.obsysapp.domain.Account;
import org.obsys.obsysapp.domain.Login;
import org.obsys.obsysapp.domain.MonthlySummary;
import org.obsys.obsysapp.domain.Payee;
import org.obsys.obsysapp.models.AccountModel;
import org.obsys.obsysapp.models.StatementModel;
import org.obsys.obsysapp.models.TransactionModel;
import org.obsys.obsysapp.views.AccountView;
import org.obsys.obsysapp.views.ViewBuilder;

import java.sql.Connection;
import java.util.ArrayList;

public class AccountController {
    private final Stage stage;
    private final ViewBuilder viewBuilder;
    private AccountModel acctModel;
    private Login login;

    public AccountController(Stage stage,
                             AccountModel acctModel,
                             Login login) {
        this.stage = stage;
        this.acctModel = acctModel;
        viewBuilder = new AccountView(acctModel, this::goHome, this::logout,
                this::goToTransactions, this::goToStatement);
        this.login = login;
    }

    private void goToStatement() {
        try (Connection conn = ObsysDbConnection.openDBConn()) {
            StatementModel stmtModel = new StatementModel(
                    new PersonDAO().readPersonByPersonId(
                            conn, login.getPersonId()),
                    new Account(
                            acctModel.getType(),
                            acctModel.getAcctNum(),
                            acctModel.getStatus(),
                            acctModel.getBalance(),
                            acctModel.getDateOpened(),
                            acctModel.getInstallment()),
                    new MonthlySummary(
                            new TransactionDAO().readTransactionsByMonth(
                                    conn,
                                    acctModel.getSelectedMonth(),
                                    acctModel.getAcctNum())),
                    acctModel.getSelectedMonth()
            );

            StatementController stmtCtrl = new StatementController(
                    stage,
                    stmtModel,
                    acctModel,
                    login
            );

            stage.setScene(new Scene(stmtCtrl.getView()));

        } catch (Exception e) {
            ErrorController eCtrl = new ErrorController(
                    stage,
                    e.getMessage(),
                    this.getView()
            );
            stage.setScene(new Scene(eCtrl.getView()));
        }
    }

    private void goToTransactions() {
        try (Connection conn = ObsysDbConnection.openDBConn()) {
            PayeeDAO payeeDao = new PayeeDAO();

            ArrayList<Payee> payees = switch (acctModel.getTransactionType()) {
                case "TF", "PY" -> payeeDao.readAccountsByPersonId(
                        conn,
                        login.getPersonId(),
                        acctModel.getAcctNum());
                default -> payeeDao.readPayeesByAccount(
                        conn,
                        acctModel.getAcctNum());
            };

            Account account = new Account(
                    acctModel.getType(),
                    acctModel.getAcctNum(),
                    acctModel.getStatus(),
                    acctModel.getBalance(),
                    acctModel.getDateOpened(),
                    acctModel.getInstallment(),
                    acctModel.getInterestDue()
            );

            TransactionModel transactionModel = new TransactionModel(
                    acctModel.getTransactionType(),
                    account,
                    payees
            );

            TransactionController transactCtrl = new TransactionController(
                    stage,
                    this.getView(),
                    transactionModel,
                    login
            );
            stage.setScene(new Scene(transactCtrl.getView()));
        } catch (Exception e) {
            ErrorController eCtrl = new ErrorController(
                    stage,
                    e.getMessage(),
                    this.getView()
            );
            stage.setScene(new Scene(eCtrl.getView()));
        }
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
