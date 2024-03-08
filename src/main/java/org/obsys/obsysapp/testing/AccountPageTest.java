package org.obsys.obsysapp.testing;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.obsys.obsysapp.controllers.AccountController;
import org.obsys.obsysapp.controllers.ErrorController;
import org.obsys.obsysapp.domain.Login;
import org.obsys.obsysapp.domain.Transaction;
import org.obsys.obsysapp.models.AccountModel;

import java.time.LocalDate;
import java.util.ArrayList;

public class AccountPageTest extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        try {
            stage.setTitle("OBSys - Online Banking Application");
            stage.getIcons().add(new Image("/obsysIcon.png"));
            stage.setResizable(false);

            // Mock data
            Login login = new Login("wwindmilla", "lH5@2S,j_)ybs", false, 1111111159);
            AccountModel sample = getSampleChecking();


            stage.setScene(new Scene(new AccountController(stage, sample, login).getView()));

        } catch (Exception e) {
            stage.setScene(new Scene(new ErrorController(stage, e).getView()));
        }
        stage.show();
    }

    private AccountModel getSampleChecking() {
        AccountModel sample = new AccountModel(1111111111);
        sample.setType("CH");
        sample.setBalance(1241.53);
        sample.setDateOpened(LocalDate.of(2023, 2, 21));
        sample.setStatus("OP");
        sample.setInterestRate(0);
        sample.setHistory(getSampleTransactions());
        return sample;
    }

    private ArrayList<Transaction> getSampleTransactions() {
        ArrayList<Transaction> history = new ArrayList<>();

        // For accurate testing, ensure dates are within test ranges

        // Within 1 week of today
        history.add(new Transaction(
                "DP", 546.25,
                LocalDate.of(2024, 3, 2),"ATM# 23453"));
        history.add(new Transaction(
                "WD", 650.21,
                LocalDate.of(2024, 3, 4), "1365487554"));
        history.add(new Transaction(
                "TR", 254.02,
                LocalDate.of(2024, 3, 1), "111111112"));

        // Within 1 month of today but beyond 1 week from today
        history.add(new Transaction(
                "DP", 845.26,
                LocalDate.of(2024, 2, 15), "1954652584"));
        history.add(new Transaction(
                "WD", 215.65,
                LocalDate.of(2024, 2, 12), "1985654875"));
        history.add(new Transaction(
                "DP", 300.56,
                LocalDate.of(2024, 2, 15), "1357954856"));


        return history;
    }
}
