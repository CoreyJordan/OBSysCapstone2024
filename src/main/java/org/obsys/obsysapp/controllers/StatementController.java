package org.obsys.obsysapp.controllers;

import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.obsys.obsysapp.domain.Login;
import org.obsys.obsysapp.models.AccountModel;
import org.obsys.obsysapp.models.StatementModel;
import org.obsys.obsysapp.views.StatementView;
import org.obsys.obsysapp.views.ViewBuilder;

public class StatementController {
    private final Stage stage;
    private final ViewBuilder viewBuilder;
    private final AccountModel acctModel;
    private final Login login;

    public StatementController(Stage stage,
                               StatementModel stmtModel,
                               AccountModel acctModel,
                               Login login) {
        this.stage = stage;
        this.acctModel = acctModel;
        this.login = login;
        viewBuilder = new StatementView(
                stmtModel,
                this::logout,
                this::goBack,
                stage
        );
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void logout() {
        stage.setScene(new Scene(new LoginController(
                stage, "dolphinExit.png", "Thank you!").getView()));
    }

    private void goBack() {
        AccountController acctCtrl = new AccountController(
                stage,
                acctModel,
                login
        );
        stage.setScene(new Scene(acctCtrl.getView()));
    }

}
