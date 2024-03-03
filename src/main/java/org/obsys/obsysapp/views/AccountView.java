package org.obsys.obsysapp.views;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class AccountView extends ViewBuilder implements IObsysBuilder {

    @Override
    public AnchorPane build() {
        AnchorPane acctWindow = super.build();
        acctWindow.getChildren().addAll(createPanels());
        acctWindow.getChildren().addAll(loadImages());
        acctWindow.getChildren().addAll(createLabels());
        acctWindow.getChildren().addAll(createButtons());

        return acctWindow;
    }

    @Override
    public ArrayList<Rectangle> createPanels() {
        ArrayList<Rectangle> panels = new ArrayList<>();

        panels.add(obsysPanel(20, 210, 580, 105));
        panels.add(obsysPanel(20, 325, 580, 215));
        panels.add(obsysPanel(610, 210, 290, 330));

        return panels;
    }

    @Override
    public ArrayList<Node> loadImages() {
        ArrayList<Node> images = new ArrayList<>();

        images.add(obsysImage("dolphinLogoBlue.png", 660, 5, 250, 205));

        return images;
    }

    @Override
    public ArrayList<Node> createLabels() {
        ArrayList<Node> labels = new ArrayList<>(createAcctLabels());
        labels.add(obsysLabel("Action Panel", 660, 215, "header3"));


        return labels;
    }

    private ArrayList<Node> createAcctLabels() {
        ArrayList<Node> labels = new ArrayList<>();

        Label lblAcctName = obsysLabel("AcctType", 40, 80, "sub-header");
        // TODO hypAcctName.textProperty().bind(acctModel.getAccountType(i));
        labels.add(lblAcctName);

        Label lblAcctNum = obsysLabel("....0000", 155, 135);
        // TODO lblAcctNum.textProperty().bind(acctModel.getAccountNum(i));
        labels.add(lblAcctNum);

        Label lblBalance = obsysLabel("Amount of Balance", 460, 120, 175);
        // TODO lblBalance.textProperty().bind(acctModel.getBalance(i));
        lblBalance.setAlignment(Pos.CENTER_RIGHT);
        labels.add(lblBalance);

        Label lblBalanceType = obsysLabel("TYPE", 460, 140, 175, "hint");
        // TODO lblBalanceType.textProperty().bind(acctModel.balanceTypeProperty(i));
        lblBalanceType.setAlignment(Pos.CENTER_RIGHT);
        labels.add(lblBalanceType);

        Label lblStatus = obsysLabel("This account is status", 295, 100, "warning");
        // TODO lblStatus.textProperty().bind(acctModel.statusProperty(i));
        labels.add(lblStatus);

        return labels;
    }

    @Override
    public ArrayList<Node> createButtons() {
        ArrayList<Node> buttons = new ArrayList<>();

        Button btnBack = obsysButton("Home", 10, 10, 100, new Image("back.png"));
        // TODO btnBack.setOnAction(evt -> returnHandler.run());
        buttons.add(btnBack);

        Hyperlink hypLogout = obsysLink("Logout", 830, 5);
        // TODO hypLogout.setOnAction(evt -> logoutHandler.run());
        buttons.add(hypLogout);

        Button btnDeposit = obsysButton("Deposit Funds", 655, 300, 200);
        // TODO deposit handler
        buttons.add(btnDeposit);

        Button btnWithdraw = obsysButton("Withdraw Funds", 655, 350, 200);
        // TODO withdraw handler
        buttons.add(btnWithdraw);

        Button btnTransfer = obsysButton("Transfer Funds", 655, 400, 200);
        // TODO transfer handler
        buttons.add(btnTransfer);

        Button btnStatement = obsysButton("Statements", 655, 475, 200);
        // TODO statement handler
        buttons.add(btnStatement);

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
}
