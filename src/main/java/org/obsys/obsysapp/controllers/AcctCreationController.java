package org.obsys.obsysapp.controllers;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Builder;
import org.obsys.obsysapp.models.CreationModel;
import org.obsys.obsysapp.models.LoginModel;
import org.obsys.obsysapp.views.AcctCreationView;

public class AcctCreationController {
    private final Stage stage;
    private final Builder<AnchorPane> viewBuilder;
    private final CreationModel creationModel;
    private final LoginModel loginModel;

    public AcctCreationController(Stage stage) {
        this.stage = stage;
        creationModel = new CreationModel();
        loginModel = new LoginModel();
        viewBuilder = new AcctCreationView(creationModel, loginModel, this::goBack);
    }

    private void goBack() {
        stage.setScene(new Scene(new LoginController(
                stage, "dolphinLogin.png", "Welcome").getView()));
    }

    public Region getView() {
        return viewBuilder.build();
    }
}
