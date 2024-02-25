package org.obsys.obsysapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.obsys.obsysapp.controllers.AcctCreationController;
import org.obsys.obsysapp.controllers.ErrorController;
import org.obsys.obsysapp.controllers.LoginController;

public class ObsysApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("OBSys - Online Banking Application");
        stage.getIcons().add(new Image("/obsysIcon.png"));
        stage.setResizable(false);
//        stage.setScene(new Scene(new LoginController(stage, "dolphinLogin.png", "Welcome").getView()));
//        stage.setScene(new Scene(new ErrorController(stage, new Exception("Oops! An illegal operation has occurred"), new LoginController(stage, "dolphinLogin.png", "Welcome").getView()).getView()));
        stage.setScene(new Scene(new AcctCreationController(stage).getView()));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
