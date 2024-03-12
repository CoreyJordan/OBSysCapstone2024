package org.obsys.obsysapp.testing;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.obsys.obsysapp.controllers.ErrorController;
import org.obsys.obsysapp.controllers.StatementController;
import org.obsys.obsysapp.domain.Account;
import org.obsys.obsysapp.domain.Login;
import org.obsys.obsysapp.domain.Person;
import org.obsys.obsysapp.models.StatementModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StatementPageTest extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        try {
            stage.setTitle("OBSys - Online Banking Application");
            stage.getIcons().add(new Image("/obsysIcon.png"));
            stage.setResizable(false);

            stage.setScene(new Scene(
                    new StatementController(stage, createSampleStatementModel()).getView()));

        } catch (Exception e) {
            stage.setScene(new Scene(new ErrorController(stage, e).getView()));

            e.printStackTrace();
        }
        stage.show();
    }

    private StatementModel createSampleStatementModel() {
        StatementModel stmtModel = new StatementModel(createSamplePerson(), createSampleCheckingAccount());
        stmtModel.setMonths(new ArrayList<>(List.of(
                LocalDate.of(2024, 3, 21),
                LocalDate.of(2024, 2, 21),
                LocalDate.of(2024, 1, 21),
                LocalDate.of(2023, 12, 21),
                LocalDate.of(2023, 11, 21)
        )));
        stmtModel.setSelectedPeriod(LocalDate.of(2024, 2, 21));
        return stmtModel;
    }

    private Account createSampleCheckingAccount() {
        return new Account(
                "CH",
                1111111111,
                "OP",
                1241.53,
                LocalDate.of(2023,2,21)
        );
    }

    private Person createSamplePerson() {
        return new Person(
                1111111149,
                "Matyushenko",
                "Laina",
                "044 Waywood Street",
                "Cheyenne",
                "WY",
                "82007",
                "307-966-5395",
                "lmatyushenko0@clickbank.net",
                new Login(
                        "lainaTest",
                        "Adm!n1",
                        false,
                        1111111149
                ));
    }
}
