package org.obsys.obsysapp.controllers;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Builder;
import org.obsys.obsysapp.data.LoginDAO;
import org.obsys.obsysapp.data.ObsysDbConnection;
import org.obsys.obsysapp.domain.Person;
import org.obsys.obsysapp.models.CreationModel;
import org.obsys.obsysapp.models.LoginModel;
import org.obsys.obsysapp.utils.AccountValidator;
import org.obsys.obsysapp.utils.LoginValidator;
import org.obsys.obsysapp.views.AcctCreationView;

import java.sql.Connection;
import java.sql.SQLException;

public class AcctCreationController {
    private final Stage stage;
    private final Builder<AnchorPane> viewBuilder;
    private final CreationModel creationModel;
    private final LoginModel loginModel;
    private final LoginDAO loginDao;
    private Person foundPerson;

    public AcctCreationController(Stage stage) {
        this.stage = stage;
        creationModel = new CreationModel();
        loginModel = new LoginModel();
        loginDao = new LoginDAO();
        viewBuilder = new AcctCreationView(
                creationModel, loginModel, this::goBack, this::findAccount, this::createLogin);
        // For testing
        creationModel.setAcctNum("1111111111");
        creationModel.setFirstName("Laina");
        creationModel.setLastName("Matyushenko");
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
        if (searchInputInvalid()) return;

        try (Connection conn = ObsysDbConnection.openDBConn()) {
            foundPerson = loginDao.readPersonByNameAndAccount(
                    conn, creationModel.getAcctNum(), creationModel.getFirstName(), creationModel.getLastName());

            if (notFound(foundPerson)) return;
            if (accountAlreadyExists(foundPerson)) return;

            enableRegistration();

        } catch (SQLException e) {
            stage.setScene(new Scene(new ErrorController(stage, e, this.getView()).getView()));
        }
    }

    private void createLogin() {
        resetWarnings();
        if (loginInputIsInvalid()) {
            return;
        }

        try (Connection conn = ObsysDbConnection.openDBConn()) {
            if (usernameUnavailable(conn)) return;

            int rowsUpdated = loginDao.insertLogin(
                    conn, foundPerson.getPersonId(), loginModel.getUsername(), loginModel.getPassword());

            if (rowsUpdated == 0) {
                throw new SQLException("There was an error linking the account.");
            }

            stage.setScene(new Scene(new LoginController(
                    stage, "dolphinLogin.png", "Success!").getView()));
        } catch (SQLException e) {
            stage.setScene(new Scene(new ErrorController(stage, e, this.getView()).getView()));
        }
    }

    private boolean usernameUnavailable(Connection conn) throws SQLException {
        if (loginDao.findUsername(conn, loginModel.getUsername()) > 0) {
            loginModel.setInvalidUsername("That username is not available");
            return true;
        }
        return false;
    }

    private boolean searchInputInvalid() {
        AccountValidator acctValidator = new AccountValidator(
                creationModel.getAcctNum(), creationModel.getFirstName(), creationModel.getLastName());

        if (!acctValidator.okToSearch()) {
            promptUser(acctValidator);
            return true;
        }
        return false;
    }

    private boolean accountAlreadyExists(Person foundPerson) {
        if (foundPerson.getUsername() != null) {
            creationModel.setNotFound("There is already a username associated with this account");
            return true;
        }
        return false;
    }

    private boolean notFound(Person foundPerson) {
        if (foundPerson.getPersonId() == 0) {
            creationModel.setNotFound("No account found with that name and/or account number");
            return true;
        }
        return false;
    }

    private void enableRegistration() {
        creationModel.setPasswordDisable(false);
        creationModel.setRegistrationDisabled(false);
    }

    private void resetWarnings() {
        creationModel.setFirstNameWarning("");
        creationModel.setLastNameWarning("");
        creationModel.setAcctNumWarning("");
        creationModel.setNotFound("");
        loginModel.setInvalidLogin("");
        loginModel.setInvalidUsername("");
        loginModel.setInvalidPassword("");
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

    private boolean loginInputIsInvalid() {
        LoginValidator validator = new LoginValidator(loginModel.getUsername(), loginModel.getPassword());
        boolean loginIsInvalid = false;
        if (!validator.usernameIsValid()) {
            loginModel.setInvalidUsername("Username cannot contain spaces or symbols");
            loginIsInvalid = true;
        }

        if (!validator.passwordIsValid()) {
            loginModel.setInvalidPassword("Password does not meet requirements");
            loginIsInvalid = true;
        }
        return loginIsInvalid;
    }

}
