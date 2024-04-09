package org.obsys.obsysapp.testing;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.obsys.obsysapp.controllers.ErrorController;
import org.obsys.obsysapp.controllers.StatementController;
import org.obsys.obsysapp.domain.*;
import org.obsys.obsysapp.models.AccountModel;
import org.obsys.obsysapp.models.StatementModel;

import java.time.LocalDate;
import java.util.ArrayList;

public class StatementPageTest extends Application {
    public static void main(String[] args) {
        launch();
    }

    private static Login getLogin() {
        return new Login(
                "lainaTest",
                "Adm!n1",
                false,
                1111111149
        );
    }

    @Override
    public void start(Stage stage) {
        try {
            stage.setTitle("OBSys - Online Banking Application");
            stage.getIcons().add(new Image("/obsysIcon.png"));
            stage.setResizable(false);

            stage.setScene(new Scene(new StatementController(
                    stage,
                    createSampleStatementModel(),
                    getSampleChecking(),
                    getLogin()).getView()));

        } catch (Exception e) {
            ErrorController eCtrl = new ErrorController(
                    stage,
                    e.getMessage()
            );
            stage.setScene(new Scene(eCtrl.getView()));
        }
        stage.show();
    }

    private StatementModel createSampleStatementModel() {
        return new StatementModel(
                createSamplePerson(),
                createSampleCheckingAccount(),
                createSampleCheckingSummary(),
                LocalDate.of(2024, 2, 21)
        );
    }

    private Account createSampleCheckingAccount() {
        return new Account(
                "CH",
                1111111111,
                "OP",
                1241.53,
                LocalDate.of(2023, 2, 21)
        );
    }

    private MonthlySummary createSampleCheckingSummary() {
        return new MonthlySummary(sampleCheckingTransactions());
    }

    private ArrayList<Transaction> sampleCheckingTransactions() {
        return new ArrayList<>() {{
            add(new Transaction(
                    "DP", 535.34, LocalDate.of(2024, 2, 4), "Sample", 752.25));
            add(new Transaction(
                    "WD", 695.34, LocalDate.of(2024, 2, 5), "Sample", 56.91));
            add(new Transaction(
                    "DP", 456.45, LocalDate.of(2024, 2, 6), "Sample", 513.36));
            add(new Transaction(
                    "DP", 25.56, LocalDate.of(2024, 2, 7), "Sample", 538.92));
            add(new Transaction(
                    "DP", 658.56, LocalDate.of(2024, 2, 8), "Sample", 1197.48));
            add(new Transaction(
                    "WD", 654.21, LocalDate.of(2024, 2, 9), "Sample", 543.27));
            add(new Transaction(
                    "DP", 25.25, LocalDate.of(2024, 2, 10), "Sample", 568.52));

        }};
    }

    private Person createSamplePerson() {
        return new Person(
                1111111149,
                "Matyushenko",
                "Laina",
                "044 Waywood Street",
                "Cheyenne",
                "WY",
                "82007");
    }

    private AccountModel getSampleChecking() {
        AccountModel sample = new AccountModel(1111111111);
        sample.setType("CH");
        sample.setBalance(1241.53);
        sample.setDateOpened(LocalDate.of(2023, 2, 21));
        sample.setStatus("OP");
        sample.setInterestRate(0);
        return sample;
    }
}
