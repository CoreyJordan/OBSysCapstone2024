package org.obsys.obsysapp.controllers;

import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.obsys.obsysapp.models.StatementModel;
import org.obsys.obsysapp.views.StatementView;
import org.obsys.obsysapp.views.ViewBuilder;

public class StatementController {
    private final Stage stage;
    private final ViewBuilder viewBuilder;
    private StatementModel stmtModel;

    public StatementController(Stage stage, StatementModel stmtModel) {
        this.stage = stage;
        this.stmtModel = stmtModel;
        viewBuilder = new StatementView(stmtModel, this::logout);
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void logout() {
        stmtModel = null;

        stage.setScene(new Scene(new LoginController(
                stage, "dolphinExit.png", "Thank you!").getView()));
    }
}
