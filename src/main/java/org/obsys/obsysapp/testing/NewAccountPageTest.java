package org.obsys.obsysapp.testing;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.obsys.obsysapp.controllers.AdminHomeController;
import org.obsys.obsysapp.controllers.ErrorController;
import org.obsys.obsysapp.controllers.NewAccountController;
import org.obsys.obsysapp.domain.Login;
import org.obsys.obsysapp.domain.Person;
import org.obsys.obsysapp.models.AdminHomeModel;
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

            Person customer = new Person(
                    1111111253,
                    "Doe",
                    "Jane",
                    "103 Belidere Rd",
                    "Waverly",
                    "VA",
                    "23456",
                    "555-867-5309",
                    "fakeEmail@email.com");

            Login login = new Login(
                    "Admin",
                    "Adm!n1",
                    true,
                    1111111253);

            AdminHomeModel adminModel = new AdminHomeModel(
                    login.getUsername());

            AdminHomeController adminCtrl = new AdminHomeController(
                    stage,
                    adminModel,
                    login
            );

            Scene returnScene = new Scene(adminCtrl.getView());

            NewAccountController newAcctCtrl = new NewAccountController(
                    stage,
                    new NewAccountModel(customer),
                    returnScene
            );

            stage.setScene(new Scene(newAcctCtrl.getView()));

        } catch (Exception e) {
            ErrorController eCtrl = new ErrorController(
                    stage,
                    e.getMessage()
            );

            stage.setScene(new Scene(eCtrl.getView()));
        }
        stage.show();
    }
}
