package org.obsys.obsysapp.testing;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.obsys.obsysapp.controllers.ErrorController;
import org.obsys.obsysapp.controllers.NewAccountController;
import org.obsys.obsysapp.models.NewAccountModel;

public class NewAccountPageTest extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        try {
            stage.setTitle("OBSys - Online Banking Application");
            stage.getIcons().add(new Image("/obsysIcon.png"));
            stage.setResizable(false);

            stage.setScene(new Scene(
                    new NewAccountController(stage, new NewAccountModel()).getView()));

        } catch (Exception e) {
            stage.setScene(new Scene(new ErrorController(stage, e.getMessage()).getView()));
        }
        stage.show();
    }
}
