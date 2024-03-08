package org.obsys.obsysapp.views;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.obsys.obsysapp.models.AccountModel;

import java.util.ArrayList;

public class AccountView extends ViewBuilder implements IObsysBuilder {
    private final AccountModel acctModel;
    private final Runnable returnHandler;
    private final Runnable logoutHandler;

    public AccountView(AccountModel acctModel, Runnable returnHandler, Runnable logoutHandler) {
        this.acctModel = acctModel;
        this.returnHandler = returnHandler;
        this.logoutHandler = logoutHandler;
    }

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
        labels.addAll(createSummaryLabels());
        labels.add(createHistoryLabels());
        labels.add(obsysLabel("Action Panel", 660, 215, "header3"));

        return labels;
    }

    private Node createHistoryLabels() {
        ScrollPane scrollPane = obsysScrollPane(25, 330, 570, 205);
        scrollPane.getStyleClass().add("history");
        VBox pane = addPane();
        scrollPane.setContent(pane);

        pane.getChildren().addAll(switch (acctModel.getType()) {
            case "CH" -> checkingHistoryLabels();
            case "SV" -> savingsHistoryLabels();
            case "IC" -> checkPlusHistoryLabels();
            case "LN" -> loanHistoryLabels();
            default -> null;
        });
        return scrollPane;
    }

    private VBox addPane() {
        VBox pane = new VBox();
        pane.setPrefWidth(555);
        return pane;
    }

    private ArrayList<Node> loanHistoryLabels() {
        return null;
    }

    private ArrayList<Node> checkPlusHistoryLabels() {
        return null;
    }

    private ArrayList<Node> savingsHistoryLabels() {
        return null;
    }

    private ArrayList<Node> checkingHistoryLabels() {
        ArrayList<Node> labels = new ArrayList<>();

        labels.add(obsysLabel("Pending Transactions", 0, 0));
        String header = String.format("%-9s %-18s %-9s %-9s \n", "Date", "Description", "Credit", "Debit");
        labels.add(obsysLabel(header, 0, 0, "table"));

        ArrayList<String> pendingTransactions = acctModel.getPendingTransactions();
        for (String s : pendingTransactions) {
            labels.add(obsysLabel(s, 0, 0, "table"));
        }

        labels.add(obsysLabel("\nPosted Transactions", 0, 0));

        ArrayList<String> postedTransactions = acctModel.getPostedTransactions();
        for (String s : postedTransactions) {
            labels.add(obsysLabel(s, 0, 0, "table"));
        }

        return labels;
    }

    private ArrayList<Node> createSummaryLabels() {
        return switch (acctModel.getType()) {
            case "CH" -> checkingSummaryLabels();
            case "SV" -> savingsSummaryLabels();
            case "IC" -> checkPlusSummaryLabels();
            case "LN" -> loanSummaryLabels();
            default -> null;
        };
    }

    private ArrayList<Node> loanSummaryLabels() {
        return null;
    }

    private ArrayList<Node> checkPlusSummaryLabels() {
        return null;
    }

    private ArrayList<Node> savingsSummaryLabels() {
        return null;
    }

    private ArrayList<Node> checkingSummaryLabels() {
        ArrayList<Node> labels = new ArrayList<>();

        String fields = "Posted Balance:\nPending Debits:\nPending Credits:\nAvailable Balance:";
        Label lblFields = obsysLabel(fields, 200, 220, 250, "right-aligned");
        labels.add(lblFields);

        Label lblPosted = obsysLabel("$#,###.##", 400, 220, 150, "right-aligned");
        lblPosted.textProperty().bind(acctModel.postedBalanceProperty());
        labels.add(lblPosted);

        Label lblDebits = obsysLabel("$###.##", 400, 242.5, 150, "right-aligned");
        lblDebits.textProperty().bind(acctModel.pendingDebitsProperty());
        labels.add(lblDebits);

        Label lblCredits = obsysLabel("$###.##", 400, 265, 150, "right-aligned");
        lblCredits.textProperty().bind(acctModel.pendingCreditsProperty());
        labels.add(lblCredits);

        Label lblAvailable = obsysLabel("$###.##", 400, 287.5, 150, "right-aligned");
        lblAvailable.textProperty().bind(acctModel.balanceProperty());
        labels.add(lblAvailable);

        return labels;
    }

    private ArrayList<Node> createAcctLabels() {
        ArrayList<Node> labels = new ArrayList<>();

        Label lblAcctName = obsysLabel("AcctType", 40, 80, "sub-header");
        lblAcctName.textProperty().bind(acctModel.typeProperty());
        labels.add(lblAcctName);

        Label lblAcctNum = obsysLabel("....0000", 155, 135);
        lblAcctNum.textProperty().bind(acctModel.acctNumProperty());
        labels.add(lblAcctNum);

        Label lblBalance = obsysLabel("Amount of Balance", 360, 120, 175);
        lblBalance.textProperty().bind(acctModel.balanceProperty());
        lblBalance.setAlignment(Pos.CENTER_RIGHT);
        labels.add(lblBalance);

        Label lblBalanceType = obsysLabel("TYPE", 360, 140, 175, "hint");
        lblBalanceType.textProperty().bind(acctModel.balanceTypeProperty());
        lblBalanceType.setAlignment(Pos.CENTER_RIGHT);
        labels.add(lblBalanceType);

        Label lblStatus = obsysLabel("This account is status", 295, 100, "warning");
        lblStatus.textProperty().bind(acctModel.statusProperty());
        labels.add(lblStatus);

        return labels;
    }

    @Override
    public ArrayList<Node> createButtons() {
        ArrayList<Node> buttons = new ArrayList<>();

        Button btnBack = obsysButton("Home", 10, 10, 100, new Image("back.png"));
        btnBack.setOnAction(evt -> returnHandler.run());
        buttons.add(btnBack);

        Hyperlink hypLogout = obsysLink("Logout", 830, 5);
        hypLogout.setOnAction(evt -> logoutHandler.run());
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
