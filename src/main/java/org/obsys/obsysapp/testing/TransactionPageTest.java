package org.obsys.obsysapp.testing;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.obsys.obsysapp.controllers.ErrorController;
import org.obsys.obsysapp.controllers.TransactionController;
import org.obsys.obsysapp.domain.Account;
import org.obsys.obsysapp.domain.Payee;
import org.obsys.obsysapp.models.TransactionModel;

import java.time.LocalDate;
import java.util.ArrayList;

public class TransactionPageTest extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        try {
            stage.setTitle("OBSys - Online Banking Application");
            stage.getIcons().add(new Image("/obsysIcon.png"));
            stage.setResizable(false);

            // Set type to DP for Deposit, WD for Withdrawal, TR for Transfer, PY for Payment
            String type = "DP";

            // Test Checking Account
            Account testChecking = new Account("CH", 1234567890, "OP", 1234.23,
                    LocalDate.of(2024, 3, 20));
            ArrayList<Payee> checkingPayees = testCheckingPayees();
            TransactionModel testModel = new TransactionModel(type, testChecking, checkingPayees);

            // Run test
            stage.setScene(new Scene(
                    new TransactionController(stage, new Region(), testModel).getView()));

        } catch (Exception e) {
            stage.setScene(new Scene(new ErrorController(stage, e.getMessage()).getView()));
        }
        stage.show();
    }

    private ArrayList<Payee> testCheckingPayees() {
        return new ArrayList<>(){{
            add(new Payee(1111111110, "First Payee"));
            add(new Payee(1111111111, "Second Payee"));
            add(new Payee(1111111112, "Third Payee"));
            add(new Payee(1111111113, "Fourth Payee"));
        }};
    }
}
