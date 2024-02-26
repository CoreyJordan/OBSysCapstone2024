package org.obsys.obsysapp.controllers;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Builder;
import org.obsys.obsysapp.data.AccountDAO;
import org.obsys.obsysapp.data.ObsysDbConnection;
import org.obsys.obsysapp.domain.Person;
import org.obsys.obsysapp.models.CreationModel;
import org.obsys.obsysapp.models.LoginModel;
import org.obsys.obsysapp.utils.AccountValidator;
import org.obsys.obsysapp.views.AcctCreationView;

import java.sql.Connection;
import java.sql.SQLException;

public class AcctCreationController {
    private final Stage stage;
    private final Builder<AnchorPane> viewBuilder;
    private final CreationModel creationModel;
    private final LoginModel loginModel;
    private final AccountDAO accountDao;

    public AcctCreationController(Stage stage) {
        this.stage = stage;
        creationModel = new CreationModel();
        loginModel = new LoginModel();
        accountDao = new AccountDAO();
        viewBuilder = new AcctCreationView(creationModel, loginModel, this::goBack, this::findAccount);
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void goBack() {
        stage.setScene(new Scene(new LoginController(
                stage, "dolphinLogin.png", "Welcome").getView()));
    }

    private void findAccount() {
        resetWarnings();
        AccountValidator acctValidator = new AccountValidator(
                creationModel.getAcctNum(), creationModel.getFirstName(), creationModel.getLastName());

        if (!acctValidator.okToSearch()) {
            promptUser(acctValidator);
            return;
        }

        try (Connection conn = ObsysDbConnection.openDBConn()){
            Person foundPerson = accountDao.readPersonByNameAndAccount(
                    conn, creationModel.getAcctNum(), creationModel.getFirstName(), creationModel.getLastName());

            if (foundPerson.getPersonId() == 0) {
                creationModel.setNotFound("No account found with that name and/or account number");
                return;
            }

            // TODO Enable Registration controls
        } catch (SQLException e) {
            stage.setScene(new Scene(new ErrorController(stage, e, this.getView()).getView()));
        }
    }

    private void resetWarnings() {
        creationModel.setFirstNameWarning("");
        creationModel.setLastNameWarning("");
        creationModel.setAcctNumWarning("");
        creationModel.setNotFound("");
    }

    private void promptUser(AccountValidator acctValidator) {

        if (!acctValidator.acctNumIsValid()) {
            creationModel.setAcctNumWarning("Invalid account number");
        }

        if (!acctValidator.nameIsValid(acctValidator.getFirstName())) {
            creationModel.setFirstNameWarning("Invalid name entry");
        }

        if (!acctValidator.nameIsValid(acctValidator.getLastName())) {
            creationModel.setLastNameWarning("Invalid name entry");
        }
    }


}
