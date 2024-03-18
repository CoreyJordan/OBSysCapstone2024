package org.obsys.obsysapp.controllers;

import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.obsys.obsysapp.models.AccountModel;
import org.obsys.obsysapp.models.StatementModel;
import org.obsys.obsysapp.views.StatementView;
import org.obsys.obsysapp.views.ViewBuilder;

import java.sql.Date;
import java.time.LocalDate;

public class StatementController {
    private final Stage stage;
    private final ViewBuilder viewBuilder;
    private StatementModel stmtModel;
    private AccountModel acctModel;

    public StatementController(Stage stage, StatementModel stmtModel, AccountModel acctModel) {
        this.stage = stage;
        this.stmtModel = stmtModel;
        this.acctModel = acctModel;
        viewBuilder = new StatementView(stmtModel, this::logout, this:: goBack);
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void logout() {
        stmtModel = null;

        stage.setScene(new Scene(new LoginController(
                stage, "dolphinExit.png", "Thank you!").getView()));
    }

    private void goBack() {
        stage.setScene(new Scene(new AccountController(stage, acctModel, stmtModel.getLogin()).getView()));
    }
}
