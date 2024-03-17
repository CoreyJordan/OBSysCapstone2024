package org.obsys.obsysapp.views;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.obsys.obsysapp.models.StatementModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StatementView extends ViewBuilder implements IObsysBuilder {
    private StatementModel stmtModel;
    private Runnable logoutHandler;
    private Runnable returnHandler;

    public StatementView(StatementModel stmtModel, Runnable logoutHandler, Runnable returnHandler) {
        this.stmtModel = stmtModel;
        this.logoutHandler = logoutHandler;
        this.returnHandler = returnHandler;
    }

    @Override
    public AnchorPane build() {
        AnchorPane stmtWindow = super.build();
        stmtWindow.getChildren().addAll(createPanels());
        stmtWindow.getChildren().addAll(loadImages());
        stmtWindow.getChildren().addAll(createLabels());
        stmtWindow.getChildren().addAll(createButtons());
        stmtWindow.getChildren().addAll(createComboBox());
        stmtWindow.getChildren().add(createScrollPane());

        return stmtWindow;
    }

    @Override
    public ArrayList<Rectangle> createPanels() {
        return new ArrayList<>(List.of(obsysPanel(270, 100, 640, 75)));
    }

    @Override
    public ArrayList<Node> loadImages() {
        return new ArrayList<>(List.of(obsysImage("dolphinLogoBlue.png", 35, 50, 170, 140)));
    }

    @Override
    public ArrayList<Node> createLabels() {
        String prompt = "Select a month and Open Statement to view that statement.\n" +
                "Press Print to print a copy or save it to file.";
        return new ArrayList<>(List.of(obsysLabel("Statements", 210, 10, "banner"),
                obsysLabel(prompt, 620, 55, 300, "hint")));
    }

    @Override
    public ArrayList<Node> createButtons() {
        Button btnBack = obsysButton("Back", 10, 10, 100, new Image("back.png"));
        btnBack.setOnAction(evt -> returnHandler.run());

        Hyperlink hypLogout = obsysLink("Logout", 830, 5);
        hypLogout.setOnAction(evt -> logoutHandler.run());

        Button btnPrint = obsysButton("Print", 740, 120, 160);
        // TODO btnPrint.setOnAction(evt -> printHandler.run());

        Button btnOpen = obsysButton("Open Statement", 570, 120, 160);
        // TODO btnOpen.setOnAction(evt -> openStmtHandler.run());

        return new ArrayList<>(List.of(btnBack, hypLogout, btnPrint, btnOpen));
    }

    private ArrayList<Node> createComboBox() {
        ComboBox<LocalDate> cmbMonths = obsysComboBox(stmtModel.getMonths(), 290, 120, 265);
        cmbMonths.setValue(stmtModel.getMonths().getFirst());

        return new ArrayList<>(List.of(cmbMonths));
    }

    private Node createScrollPane() {
        ScrollPane stmtScrollPane = obsysScrollPane(10, 200, 900, 340);
        stmtScrollPane.getStyleClass().add("statement");
        stmtScrollPane.setContent(createStatementAnchorPane());

        return stmtScrollPane;
    }

    private AnchorPane createStatementAnchorPane() {
        AnchorPane stmtPane = new AnchorPane();
        stmtPane.setLayoutX(20);
        stmtPane.setLayoutY(20);
        stmtPane.setPrefWidth(880);
        stmtPane.setPrefHeight(335);

        stmtModel.setValidMonth(true);  // For building, remove after testing
        if (stmtModel.isValidMonth()) {
            // TODO Pass month into createStatement
            stmtPane.getChildren().addAll(createStatement());
        }
        return stmtPane;
    }

    private ArrayList<Node> createStatement() {
        String stmtPeriod = "ACCOUNT NUMBER:\nSTATEMENT DATE:\nPERIOD:";
        String nameAndAddress = stmtModel.getNameAndAddress();

        Label lblAcctNum = obsysLabel("XXXXXXXXXX", 320, 210);
        lblAcctNum.textProperty().bind(stmtModel.accountNumProperty());

        Label lblStmtDate = obsysLabel("MMMM dd, yyyy", 320, 232);
        lblStmtDate.textProperty().bind(stmtModel.stmtDateProperty());

        Label lblStmtPeriod = obsysLabel("Month to Month", 320, 253);
        lblStmtPeriod.textProperty().bind(stmtModel.stmtPeriodProperty());

        return new ArrayList<>(List.of(lblAcctNum, lblStmtDate, lblStmtPeriod)){{
            add(obsysImage("dolphinLogoBlue.png", 120, 20, 200, 180));
            add(obsysLabel("OBSys Banking", 330, 50, "header3"));
            add(obsysLabel("1234 Street St.\nOuter Banks, NC 27959", 330, 100));
            add(obsysLabel("STATEMENT OF ACCOUNT", 550, 165));
            add(obsysLabel(stmtPeriod, 120, 210, "right-aligned"));
            add(obsysLabel(nameAndAddress, 120, 320));
            add(createMonthlySummary());
            add(createTransactionsTable());
        }};
    }

    private Node createMonthlySummary() {
        VBox vbox = new VBox();
        vbox.setLayoutX(120);
        vbox.setLayoutY(420);


        vbox.getChildren().add(obsysLabel("Account Summary", 0, 0, "header3"));

        Label lblSummary = obsysLabel("", 0, 0, "table");
        lblSummary.textProperty().bind(stmtModel.summaryProperty());
        vbox.getChildren().add(lblSummary);

        return vbox;
    }

    private Node createTransactionsTable() {
        VBox vbox = new VBox();
        vbox.setLayoutX(120);
        vbox.setLayoutY(620);

        vbox.getChildren().add(obsysLabel("Transactions", 0, 0, "header3"));

        Label lblTransactions = obsysLabel("", 0, 0, "table");
        lblTransactions.textProperty().bind(stmtModel.transactionsProperty());
        vbox.getChildren().add(lblTransactions);

        return vbox;
    }



    // No text fields or passwords utilized on this page
    @Override
    public ArrayList<Node> createTextFields() {
        return null;
    }

    @Override
    public ArrayList<Node> buildPasswordField() {
        return null;
    }
}
