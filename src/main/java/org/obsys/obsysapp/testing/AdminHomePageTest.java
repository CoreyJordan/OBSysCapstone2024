package org.obsys.obsysapp.testing;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.obsys.obsysapp.controllers.AdminHomeController;
import org.obsys.obsysapp.controllers.ErrorController;
import org.obsys.obsysapp.models.AdminHomeModel;

public class AdminHomePageTest extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        try {
            stage.setTitle("OBSys - Online Banking Application");
            stage.getIcons().add(new Image("/obsysIcon.png"));
            stage.setResizable(false);

            stage.setScene(new Scene(new AdminHomeController(stage, new AdminHomeModel()).getView()));

        } catch (Exception e) {
            stage.setScene(new Scene(new ErrorController(stage, e.getMessage()).getView()));
        }
        stage.show();
    }
}
