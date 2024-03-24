package org.obsys.obsysapp.views;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import org.obsys.obsysapp.models.TransactionModel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TransactionView extends ViewBuilder implements IObsysBuilder {
    private final TransactionModel transactionModel;
    private final Runnable logoutHandler;
    private final Runnable returnHandler;
    private final Runnable transactionHandler;

    public TransactionView(TransactionModel transactionModel, Runnable logoutHandler, Runnable returnHandler,
                           Runnable transactionHandler) {
        this.transactionModel = transactionModel;
        this.logoutHandler = logoutHandler;
        this.returnHandler = returnHandler;
        this.transactionHandler = transactionHandler;
    }

    @Override
    public AnchorPane build() {
        AnchorPane transactionWindow = super.build();

        transactionWindow.getChildren().addAll(createPanels());
        transactionWindow.getChildren().addAll(createLabels());
        transactionWindow.getChildren().addAll(loadImages());
        transactionWindow.getChildren().addAll(createButtons());
        transactionWindow.getChildren().addAll(createPayeeCombo());
        transactionWindow.getChildren().addAll(createTextFields());

        return transactionWindow;
    }

    @Override
    public ArrayList<Rectangle> createPanels() {
        return new ArrayList<>() {{
            add(obsysPanel(275, 150, 620, 100));
            add(obsysPanel(275, 275, 340, 250));
        }};
    }

    @Override
    public ArrayList<Node> loadImages() {
        return new ArrayList<>() {{
            add(obsysImage("dolphinTransact.jpg", 40, 5, 210, 540));
            add(obsysImage("dolphinLogoBlue.png", 700, 365, 220, 175));
        }};
    }

    @Override
    public ArrayList<Node> createLabels() {
        Label lblBanner = obsysLabel("Deposit Funds", 270, 75, "sub-header");
        lblBanner.textProperty().bind(transactionModel.transactionTypeProperty());

        Label lblPayeeError = obsysLabel("", 625, 295, 270, "warning");
        lblPayeeError.textProperty().bind(transactionModel.payeeErrorProperty());

        Label lblAmountError = obsysLabel("", 625, 345, 270, "warning");
        lblAmountError.textProperty().bind(transactionModel.amountErrorProperty());

        return new ArrayList<>(List.of(lblBanner, lblPayeeError, lblAmountError)) {{
            addAll(addAcctLabels());
        }};
    }

    @Override
    public ArrayList<Node> createButtons() {
        Hyperlink hypLogout = obsysLink("Logout", 830, 5);
        hypLogout.setOnAction(evt -> logoutHandler.run());

        Button btnCancel = obsysButton("Cancel", 350, 465, 120);
        btnCancel.setOnAction(evt -> returnHandler.run());

        Button btnAction = obsysButton("Action", 485, 465, 120);
        btnAction.textProperty().bind(transactionModel.actionProperty());
        btnAction.setOnAction(evt -> transactionHandler.run());

        return new ArrayList<>(List.of(hypLogout, btnCancel, btnAction));
    }

    @Override
    public ArrayList<Node> createTextFields() {
        TextField txtAmount = obsysTextField(405, 340, 200);
        txtAmount.setAlignment(Pos.CENTER_RIGHT);
        txtAmount.textProperty().bindBidirectional(transactionModel.transactionAmountProperty());
        txtAmount.setOnMouseClicked(evt -> txtAmount.selectAll());

        // Force the user into currency entries
        Pattern pattern = Pattern.compile("\\d*|\\d+\\.\\d{0,2}");
        TextFormatter<Object> formatter = new TextFormatter<>(change ->
                pattern.matcher(change.getControlNewText()).matches() ? change : null);
        txtAmount.setTextFormatter(formatter);

        Label lblAmount = obsysLabel("AMOUNT", 425, 342, "hint");

        DatePicker dtpDate = obsysDatePicker(465, 395, 140);
        dtpDate.valueProperty().bindBidirectional(transactionModel.transactionDateProperty());
        // Force user to pick date, safe input
        dtpDate.setEditable(false);
        Label lblDate = obsysLabel("DATE", 485, 397, "hint");

        return new ArrayList<>(List.of(txtAmount, lblAmount, dtpDate, lblDate));
    }

    @Override
    public ArrayList<Node> buildPasswordField() {
        return null;
    }

    private ArrayList<Node> addAcctLabels() {
        Label lblAcctName = obsysLabel("AcctType", 285, 160, "sub-header");
        lblAcctName.textProperty().bind(transactionModel.accountTypeProperty());

        Label lblAcctNum = obsysLabel("....0000", 390, 215);
        lblAcctNum.textProperty().bind(transactionModel.accountNumProperty());

        Label lblBalance = obsysLabel("Amount of Balance", 705, 190, 175);
        lblBalance.textProperty().bind(transactionModel.balanceProperty());
        lblBalance.setAlignment(Pos.CENTER_RIGHT);

        Label lblBalanceType = obsysLabel("TYPE", 705, 210, 175, "hint");
        lblBalanceType.textProperty().bind(transactionModel.balanceTypeProperty());
        lblBalanceType.setAlignment(Pos.CENTER_RIGHT);

        Label lblStatus = obsysLabel("This account is status", 540, 170, "panel-warning");
        lblStatus.textProperty().bind(transactionModel.statusProperty());

        Label lblPaymentDate = obsysLabel("", 540, 200);
        lblPaymentDate.textProperty().bind(transactionModel.payDateProperty());

        Label lblAmountDue = obsysLabel("", 555, 220);
        lblAmountDue.textProperty().bind(transactionModel.amountDueProperty());

        return new ArrayList<>(List.of(
                lblAcctName, lblAcctNum, lblBalance, lblBalanceType, lblStatus, lblPaymentDate, lblAmountDue));
    }

    private ArrayList<Node> createPayeeCombo() {
        ComboBox<String> cmbPayee = obsysPayeeCombo(transactionModel.getPayeeDescriptions(), 285, 285, 320);
        cmbPayee.setValue(transactionModel.getPayeeDescriptions().getFirst());
        if (cmbPayee.getValue().isEmpty()) {
            cmbPayee.setEditable(true);
        }

        // This event ensures user cannot edit payees in the list from this selector.
        cmbPayee.setOnAction(evt -> cmbPayee.setEditable(cmbPayee.getValue().isEmpty()));

        cmbPayee.valueProperty().bindBidirectional(transactionModel.transactionPayeeProperty());

        Label lblPayeeType = obsysLabel("PAYER", 305, 287, "hint");
        lblPayeeType.textProperty().bind(transactionModel.payeeTypeProperty());

        return new ArrayList<>(List.of(cmbPayee, lblPayeeType));
    }
}
