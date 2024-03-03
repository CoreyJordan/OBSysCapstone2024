package org.obsys.obsysapp.views;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import org.obsys.obsysapp.domain.Login;
import org.obsys.obsysapp.models.AccountsModel;

import java.util.ArrayList;

public class HomeView extends ViewBuilder implements IObsysBuilder {

    private final Login user;
    private final AccountsModel accountsModel;
    private final Runnable logoutHandler;
    private final Runnable navigationHandler;

    public HomeView(Login user, AccountsModel acctModel, Runnable logoutHandler, Runnable navigationHandler) {
        this.user = user;
        this.accountsModel = acctModel;
        this.logoutHandler = logoutHandler;
        this.navigationHandler = navigationHandler;
    }

    @Override
    public AnchorPane build() {
        AnchorPane homeWindow = super.build();
        homeWindow.getChildren().add(createPane());
        homeWindow.getChildren().addAll(loadImages());
        homeWindow.getChildren().addAll(createButtons());
        homeWindow.getChildren().addAll(createLabels());

        return homeWindow;
    }

    @Override
    public ArrayList<Rectangle> createPanels() {

        return null;
    }

    public ScrollPane createPane() {
        ScrollPane accountsPanel = obsysScrollPane(250, 130, 665, 450);
        AnchorPane pane = addPane();
        accountsPanel.setContent(pane);

        for (int i = 0; i < accountsModel.accountsCount(); i++) {
            pane.getChildren().add(obsysPanel(10, 10 + (i * 120), 620, 100));
            pane.getChildren().addAll(addAcctLabels(i));
        }

        return accountsPanel;
    }

    @Override
    public ArrayList<Node> loadImages() {
        ArrayList<Node> images = new ArrayList<>();

        images.add(obsysImage("dolphinLogoBlue.png", 10, 465, 230, 185));


        return images;
    }

    @Override
    public ArrayList<Node> createLabels() {
        ArrayList<Node> labels = new ArrayList<>();

        labels.add(obsysLabel("Welcome " + user.getUsername(), 20, 10, "sub-header"));
        labels.add(obsysLabel("Accounts Home", 400, 50, "banner"));

        String instructions = "Select an account to view more details or to perform transactions.";
        labels.add(obsysLabel(instructions, 20, 140, 230));

        return labels;
    }

    @Override
    public ArrayList<Node> createButtons() {
        ArrayList<Node> buttons = new ArrayList<>();

        Hyperlink hypLogout = obsysLink("Logout", 830, 5);
        hypLogout.setOnAction(evt -> logoutHandler.run());
        buttons.add(hypLogout);

        return buttons;
    }

    @Override
    public ArrayList<Node> createTextFields() {
        return null;
    }

    @Override
    public ArrayList<Node> buildPasswordField() {
        return null;
    }

    private AnchorPane addPane() {
        AnchorPane pane = new AnchorPane();
        pane.setPrefWidth(650);
        pane.setPrefHeight(400);
        return pane;
    }

    private ArrayList<Node> addAcctLabels(int i) {
        ArrayList<Node> labels = new ArrayList<>();

        Hyperlink hypAcctName = obsysLink("AcctType", 10, 10 + (i * 120), "sub-header");
        hypAcctName.textProperty().bind(accountsModel.getAccountType(i));
        hypAcctName.setOnAction(evt -> {
            accountsModel.setTargetAccountNumber(accountsModel.getAcctNum(i));
            navigationHandler.run();
        });
        labels.add(hypAcctName);

        Label lblAcctNum = obsysLabel("....0000", 125, 75 + (i * 120));
        lblAcctNum.textProperty().bind(accountsModel.getAccountNumLast4(i));
        labels.add(lblAcctNum);

        Label lblBalance = obsysLabel("Amount of Balance", 440, 50 + (i * 120), 175);
        lblBalance.textProperty().bind(accountsModel.getBalance(i));
        lblBalance.setAlignment(Pos.CENTER_RIGHT);
        labels.add(lblBalance);

        Label lblBalanceType = obsysLabel("TYPE", 440, 70 + (i * 120), 175, "hint");
        lblBalanceType.textProperty().bind(accountsModel.balanceTypeProperty(i));
        lblBalanceType.setAlignment(Pos.CENTER_RIGHT);
        labels.add(lblBalanceType);

        Label lblStatus = obsysLabel("This account is status", 275, 30 + (i * 120), "panel-warning");
        lblStatus.textProperty().bind(accountsModel.statusProperty(i));
        labels.add(lblStatus);

        Label lblPaymentDate = obsysLabel("", 275, 60 + (i * 120));
        lblPaymentDate.textProperty().bind(accountsModel.payDateProperty(i));
        labels.add(lblPaymentDate);

        Label lblAmountDue = obsysLabel("", 290, 80 + (i * 120));
        lblAmountDue.textProperty().bind(accountsModel.amountDueProperty(i));
        labels.add(lblAmountDue);

        return labels;
    }
}
