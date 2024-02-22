package org.obsys.obsysapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.obsys.obsysapp.controllers.LoginController;

public class ObsysApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("OBSys - Online Banking Application");
        stage.getIcons().add(new Image("/obsysIcon.png"));
        stage.setResizable(false);
        stage.setScene(new Scene(new LoginController().getView()));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
