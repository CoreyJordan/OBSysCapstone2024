package org.obsys.obsysapp.controllers;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Builder;
import org.obsys.obsysapp.data.LoginDAO;
import org.obsys.obsysapp.data.ObsysDbConnection;
import org.obsys.obsysapp.domain.Login;
import org.obsys.obsysapp.models.LoginModel;
import org.obsys.obsysapp.utils.LoginValidator;
import org.obsys.obsysapp.views.LoginView;

import java.sql.Connection;
import java.sql.SQLException;

public class LoginController {
    private final Stage stage;
    private final Builder<AnchorPane> viewBuilder;
    private final LoginDAO loginDao;
    private final LoginModel loginModel;


    public LoginController(Stage stage, String imgSource, String bannerText) {
        this.stage = stage;
        loginModel = new LoginModel();
        loginDao = new LoginDAO();
        viewBuilder = new LoginView(loginModel, this::lookupLogin, this::openCreation, imgSource, bannerText);
    }

    private static void navigateToHomePage(Login checkedLogin) {
        if (checkedLogin.isAdmin()) {
            // TODO open Admin Home Page
            System.out.println("Loading Admin Home");
        } else {
            // TODO open User Home Page
            System.out.println("Loading home page");
        }
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void lookupLogin() {
        if (loginInputIsInvalid()) return;

        try (Connection conn = ObsysDbConnection.openDBConn()) {
            Login checkedLogin = loginDao.readPasswordByUsername(conn, loginModel.getUsername());

            if (loginMatchNotFound(checkedLogin)) return;

            navigateToHomePage(checkedLogin);

        } catch (SQLException e) {
            // Erase password from model for security.
            loginModel.setPassword("");
            stage.setScene(new Scene(new ErrorController(stage, e, this.getView()).getView()));
        }
    }

    private boolean loginMatchNotFound(Login checkedLogin) {
        if (!passwordsMatch(checkedLogin)) {
            loginModel.setInvalidLogin("Username or password matches no record");
            return true;
        }
        return false;
    }

    private boolean loginInputIsInvalid() {
        LoginValidator validator = new LoginValidator(loginModel.getUsername(), loginModel.getPassword());
        if (!validator.okToLogin()) {
            loginModel.setInvalidLogin("Invalid username and/or password");
            return true;
        }
        return false;
    }

    private void openCreation() {
        stage.setScene(new Scene(new AcctCreationController(stage).getView()));
    }

    private boolean passwordsMatch(Login checkedLogin) {
        return checkedLogin.getPassword().equals(loginModel.getPassword());
    }
}

