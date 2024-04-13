package org.obsys.obsysapp.views;

import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.obsys.obsysapp.models.StatementModel;
import javafx.scene.transform.Scale;

import java.util.ArrayList;
import java.util.List;

public class StatementView extends ViewBuilder implements IObsysBuilder {
    private final StatementModel stmtModel;
    private final Runnable logoutHandler;
    private final Runnable returnHandler;
    private final Stage stage;

    public StatementView(StatementModel stmtModel,
                         Runnable logoutHandler,
                         Runnable returnHandler,
                         Stage stage) {
        this.stmtModel = stmtModel;
        this.logoutHandler = logoutHandler;
        this.returnHandler = returnHandler;
        this.stage = stage;
    }

    @Override
    public AnchorPane build() {
        AnchorPane stmtWindow = super.build();
        stmtWindow.getChildren().addAll(loadImages());
        stmtWindow.getChildren().addAll(createLabels());

        Node stmtPane = createStatementAnchorPane();
        stmtWindow.getChildren().add(createScrollPane(stmtPane));

        stmtWindow.getChildren().addAll(createButtons(stmtPane));

        return stmtWindow;
    }


    @Override
    public ArrayList<Node> loadImages() {
        return new ArrayList<>(List.of(
                obsysImage("dolphinLogoBlue.png", 35, 50, 170, 140)));
    }

    @Override
    public ArrayList<Node> createLabels() {
        String prompt = "Select a month and Open Statement to view that statement.\n" +
                "Press Print to print a copy or save it to file.";
        return new ArrayList<>(List.of(
                obsysLabel("Statement", 210, 10, "banner"),
                obsysLabel(prompt, 620, 55, 300, "hint")));
    }

    public ArrayList<Node> createButtons(Node printableNode) {
        Button btnBack = obsysButton(
                "Back", 10, 10, 100, new Image("back.png"));
        btnBack.setOnAction(evt -> returnHandler.run());

        Hyperlink hypLogout = obsysLink("Logout", 830, 5);
        hypLogout.setOnAction(evt -> logoutHandler.run());

        Button btnPrint = obsysButton("Print", 740, 120, 160);
        btnPrint.setOnAction(evt -> print(printableNode));

        return new ArrayList<>(List.of(btnBack, hypLogout, btnPrint));
    }

    private Node createScrollPane(Node node) {
        ScrollPane stmtScrollPane = obsysScrollPane(100, 200, 710, 340);
        stmtScrollPane.getStyleClass().add("statement");
        stmtScrollPane.setContent(node);

        return stmtScrollPane;
    }

    private AnchorPane createStatementAnchorPane() {
        AnchorPane stmtPane = new AnchorPane();
        stmtPane.setLayoutX(0);
        stmtPane.setLayoutY(0);
        stmtPane.setPrefWidth(690);
        stmtPane.setPrefHeight(335);
        stmtPane.getChildren().addAll(createStatement());

        return stmtPane;
    }

    private ArrayList<Node> createStatement() {
        String stmtPeriod = "ACCOUNT NUMBER:\nSTATEMENT DATE:\nPERIOD:";
        String nameAndAddress = stmtModel.getNameAndAddress();

        Label lblAcctNum = obsysLabel("XXXXXXXXXX", 200, 210);
        lblAcctNum.textProperty().bind(stmtModel.accountNumProperty());

        Label lblStmtDate = obsysLabel("MMMM dd, yyyy", 200, 232);
        lblStmtDate.textProperty().bind(stmtModel.stmtDateProperty());

        Label lblStmtPeriod = obsysLabel("Month to Month", 200, 253);
        lblStmtPeriod.textProperty().bind(stmtModel.stmtPeriodProperty());

        return new ArrayList<>(List.of(
                lblAcctNum,
                lblStmtDate,
                lblStmtPeriod,
                obsysImage("dolphinLogoBlue.png", 10, 20, 200, 180),
                obsysLabel("OBSys Banking", 220, 50, "header3"),
                obsysLabel("1234 Street St.\nOuter Banks, NC 27959", 220, 100),
                obsysLabel("STATEMENT OF ACCOUNT", 430, 165),
                obsysLabel(stmtPeriod, 10, 210, "right-aligned"),
                obsysLabel(nameAndAddress, 10, 320),
                createMonthlySummary(),
                createTransactionsTable()));
    }

    private Node createMonthlySummary() {
        VBox vbox = new VBox();
        vbox.setLayoutX(10);
        vbox.setLayoutY(420);


        vbox.getChildren().add(obsysLabel("Account Summary", 0, 0, "header3"));

        Label lblSummary = obsysLabel("", 0, 0, "table");
        lblSummary.textProperty().bind(stmtModel.summaryProperty());
        vbox.getChildren().add(lblSummary);

        return vbox;
    }

    private Node createTransactionsTable() {
        VBox vbox = new VBox();
        vbox.setLayoutX(10);
        vbox.setLayoutY(620);

        vbox.getChildren().add(obsysLabel("Transactions", 0, 0, "header3"));

        Label lblTransactions = obsysLabel("", 0, 0, "table");
        lblTransactions.textProperty().bind(stmtModel.transactionsProperty());
        vbox.getChildren().add(lblTransactions);

        return vbox;
    }

    private void print(Node node) {
        PrinterJob job = PrinterJob.createPrinterJob();
        PageLayout layout = job.getPrinter().createPageLayout(
                Paper.NA_LETTER,
                PageOrientation.PORTRAIT,
                Printer.MarginType.DEFAULT);

        double ratio = layout.getPrintableHeight() /
                node.getBoundsInParent().getHeight() * .60;
        Scale scale = new Scale(ratio, ratio);
        node.getTransforms().add(scale);

        job.showPrintDialog(stage);
        job.printPage(layout, node);
        job.endJob();
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

    @Override
    public ArrayList<Rectangle> createPanels() {
        return null;
    }

    @Override
    public ArrayList<Node> createButtons() {
        return null;
    }
}
