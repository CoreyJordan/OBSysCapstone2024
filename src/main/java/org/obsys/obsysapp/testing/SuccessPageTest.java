package org.obsys.obsysapp.testing;

import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.obsys.obsysapp.controllers.ErrorController;
import org.obsys.obsysapp.controllers.SuccessController;
import org.obsys.obsysapp.domain.Account;
import org.obsys.obsysapp.domain.Transaction;
import org.obsys.obsysapp.models.SuccessModel;

import java.time.LocalDate;

public class SuccessPageTest extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage){
        try {
            stage.setTitle("OBSys - Online Banking Application");
            stage.getIcons().add(new Image("/obsysIcon.png"));
            stage.setResizable(false);

            stage.setScene(new Scene(
                    new SuccessController(stage,
                            new SuccessModel(sampleDeposit(), sampleChecking())).getView()));

        } catch (Exception e) {
            stage.setScene(new Scene(new ErrorController(stage, e.getMessage()).getView()));
        }
        stage.show();
    }

    private Account sampleChecking() {
        return new Account("CH", 1234567890, "OP", 1234.23,
                LocalDate.of(2024, 3, 20));
    }

    private Transaction sampleDeposit() {
        return new Transaction("DP", 100.00, LocalDate.now(), "ATM 12345", 250.00);
    }
}
