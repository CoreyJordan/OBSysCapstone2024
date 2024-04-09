package org.obsys.obsysapp.controllers;

import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.obsys.obsysapp.models.ErrorModel;
import org.obsys.obsysapp.views.ErrorView;
import org.obsys.obsysapp.views.ViewBuilder;

public class ErrorController {
    private final Stage stage;
    private final ViewBuilder viewBuilder;
    private final Region previousPage;

    public ErrorController(Stage stage, String error, Region previousPage) {
        this.previousPage = previousPage;
        this.stage = stage;
        viewBuilder = new ErrorView(
                new ErrorModel(error),
                this::exitApp,
                this::logout,
                this::goBack);
    }

    public ErrorController(Stage stage, String error) {
        this.previousPage = null;
        this.stage = stage;
        viewBuilder = new ErrorView(
                new ErrorModel(error),
                this::exitApp,
                this::logout,
                this::goBack);
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void goBack() {
        if (previousPage == null) {
            return;
        }
        stage.setScene(new Scene(previousPage));
    }

    private void logout() {
        LoginController login = new LoginController(
                stage,
                "dolphinExit.png",
                "Thank you!");
        stage.setScene(new Scene(login.getView()));
    }

    private void exitApp() {
        System.exit(0);
    }


}
