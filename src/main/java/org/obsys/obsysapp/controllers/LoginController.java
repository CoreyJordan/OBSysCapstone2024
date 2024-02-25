package org.obsys.obsysapp.controllers;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Builder;
import org.obsys.obsysapp.data.LoginDAO;
import org.obsys.obsysapp.data.ObsysDb;
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


    public LoginController(Stage stage) {
        this.stage = stage;
        loginModel = new LoginModel();
        loginDao = new LoginDAO();
        viewBuilder = new LoginView(loginModel, this::lookupLogin);
    }

    public Region getView() {
        return viewBuilder.build();
    }

    public void lookupLogin() {
        LoginValidator validator = new LoginValidator(loginModel.getUsername(), loginModel.getPassword());
        if (!validator.okToLogin()) {
            loginModel.setInvalidLogin("Invalid username and/or password");
            return;
        }

        try (Connection conn = ObsysDb.openDBConn()) {
            Login checkedLogin = loginDao.readPasswordByUsername(conn, loginModel.getUsername());

            if (!passwordsMatch(checkedLogin)) {
                loginModel.setInvalidLogin("Username or password matches no record");
            } else {
                System.out.println("Loading home page");
                // TODO open next page
            }
        } catch (SQLException e) {
            stage.setScene(new Scene(new ErrorController(stage, e).getView()));
        }
    }


    private boolean passwordsMatch(Login checkedLogin) {
        return checkedLogin.getPassword().equals(loginModel.getPassword());
    }
}

