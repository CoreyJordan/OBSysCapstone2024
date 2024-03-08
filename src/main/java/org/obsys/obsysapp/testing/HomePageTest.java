package org.obsys.obsysapp.testing;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.obsys.obsysapp.controllers.ErrorController;
import org.obsys.obsysapp.controllers.HomeController;
import org.obsys.obsysapp.domain.Login;

public class HomePageTest extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        try {
            stage.setTitle("OBSys - Online Banking Application");
            stage.getIcons().add(new Image("/obsysIcon.png"));
            stage.setResizable(false);

            Login testUser = new Login("Laina", "Adm!n1", false, 1111111149);
            stage.setScene(new Scene(new HomeController(stage, testUser).getView()));

        } catch (Exception e) {
            stage.setScene(new Scene(new ErrorController(stage, e).getView()));
        }
        stage.show();
    }
}
