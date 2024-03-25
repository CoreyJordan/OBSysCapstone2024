package org.obsys.obsysapp.controllers;

import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.obsys.obsysapp.models.SuccessModel;
import org.obsys.obsysapp.views.SuccessView;
import org.obsys.obsysapp.views.ViewBuilder;

public class SuccessController {
    private final Stage stage;
    private final ViewBuilder viewBuilder;
    private final SuccessModel successModel;

    public SuccessController(Stage stage, SuccessModel successModel) {
        this.stage = stage;
        this.successModel = successModel;
        viewBuilder = new SuccessView(successModel, this::logout, this::goBack);
    }

    private void logout() {
        stage.setScene(new Scene(new LoginController(
                stage, "dolphinExit.png", "Thank you!").getView()));
    }

    private void goBack() {
        // TODO return to account
    }

    public Region getView() {
        return viewBuilder.build();
    }
}
