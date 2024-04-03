package org.obsys.obsysapp.testing;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.obsys.obsysapp.controllers.AdminHomeController;
import org.obsys.obsysapp.controllers.ErrorController;
import org.obsys.obsysapp.domain.Login;
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

            Login login = new Login("Admin", "Adm!n1", true, 1111122222);
            AdminHomeModel adminModel = new AdminHomeModel(login.getUsername());

            stage.setScene(new Scene(new AdminHomeController(stage, adminModel, login).getView()));

        } catch (Exception e) {
            stage.setScene(new Scene(new ErrorController(stage, e.getMessage()).getView()));
            e.printStackTrace();
        }
        stage.show();
    }
}
