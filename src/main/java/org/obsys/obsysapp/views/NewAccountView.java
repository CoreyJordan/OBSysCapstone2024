package org.obsys.obsysapp.views;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import org.obsys.obsysapp.models.NewAccountModel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class NewAccountView extends ViewBuilder implements IObsysBuilder {
    private final NewAccountModel newAccountModel;
    private final Runnable logoutHandler;
    private final Runnable clearFormHandler;
    private final Runnable returnHandler;
    private final Runnable newCustomerHandler;
    private final Runnable registrationHandler;
    private final Runnable openAcctHandler;
    private final Runnable acctTypeSelectionHandler;


    public NewAccountView(
            NewAccountModel newAccountModel,
            Runnable logoutHandler,
            Runnable clearFormHandler,
            Runnable returnHandler,
            Runnable newCustomerHandler,
            Runnable registrationHandler,
            Runnable openAcctHandler,
            Runnable acctTypeSelectionHandler) {
        this.newAccountModel = newAccountModel;
        this.logoutHandler = logoutHandler;
        this.clearFormHandler = clearFormHandler;
        this.returnHandler = returnHandler;
        this.newCustomerHandler = newCustomerHandler;
        this.registrationHandler = registrationHandler;
        this.openAcctHandler = openAcctHandler;
        this.acctTypeSelectionHandler = acctTypeSelectionHandler;
    }

    @Override
    public AnchorPane build() {
        AnchorPane newAccountWindow = super.build();
        newAccountWindow.getChildren().addAll(createPanels());
        newAccountWindow.getChildren().addAll(loadImages());
        newAccountWindow.getChildren().addAll(createLabels());
        newAccountWindow.getChildren().addAll(createButtons());
        newAccountWindow.getChildren().addAll(createTextFields());

        return newAccountWindow;
    }

    @Override
    public ArrayList<Rectangle> createPanels() {
        return new ArrayList<>() {{
            add(obsysPanel(10, 75, 200, 465));
            add(obsysPanel(220, 75, 480, 465));
        }};
    }

    @Override
    public ArrayList<Node> loadImages() {
        return new ArrayList<>() {{
            add(obsysImage("dolphinLogoBlue.png", 710, 345, 225, 190));
        }};
    }

    @Override
    public ArrayList<Node> createLabels() {
        Label lblDateTime = obsysLabel("", 30, 5);
        lblDateTime.textProperty().bind(newAccountModel.dateProperty());

        Label lblErrors = obsysLabel("", 230, 500, "panel-warning");
        lblErrors.textProperty().bind(newAccountModel.errorMessageProperty());

        String instructions = """
                Verify Customer info or Register New Customer.

                Account number and payment amounts will appear after creation.
                """;
        Label lblInstructions = obsysLabel(instructions, 720, 100, 190);
        lblInstructions.setWrapText(true);

        return new ArrayList<>(
                List.of(lblDateTime, lblErrors, lblInstructions)) {{
            add(obsysLabel("New Account", 295, 20, "sub-header"));
            add(obsysLabel("Customer Information", 240, 90));
        }};
    }

    @Override
    public ArrayList<Node> createButtons() {
        Hyperlink hypLogout = obsysLink("Logout", 830, 5);
        hypLogout.setOnAction(evt -> logoutHandler.run());

        Hyperlink hypClear = obsysLink("Clear", 650, 54);
        hypClear.setOnAction(evt -> clearFormHandler.run());

        Hyperlink hypNewCustomer = obsysLink("New Customer?", 15, 80);
        hypNewCustomer.setOnAction(evt -> newCustomerHandler.run());

        Image imgBack = new Image("back.png");
        Button btnBack = obsysButton("Home", 10, 30, 100, imgBack);
        btnBack.setOnAction(evt -> returnHandler.run());

        Button btnRegister = obsysButton("Register", 105, 425, 100);
        btnRegister.disableProperty().bind(
                newAccountModel.newCustomerFieldsDisabledProperty());
        btnRegister.setOnAction(evt -> registrationHandler.run());

        Button btnOpenAcct = obsysButton("Open Account", 535, 445, 150);
        btnOpenAcct.setOnAction(evt -> openAcctHandler.run());

        return new ArrayList<>(
                List.of(hypClear, hypLogout, hypNewCustomer, btnBack,
                        btnRegister, btnOpenAcct));
    }

    @Override
    public ArrayList<Node> createTextFields() {
        return new ArrayList<>() {{
            addAll(createNewCustomerFields());
            addAll(createInfoFields());
            addAll(createAccountFields());
        }};
    }

    private ArrayList<Node> createNewCustomerFields() {
        TextField txtFirstName = obsysTextField(15, 110, 190);
        txtFirstName.disableProperty().bind(
                newAccountModel.newCustomerFieldsDisabledProperty());
        txtFirstName.textProperty().bindBidirectional(
                newAccountModel.newFirstNameProperty());
        Label lblFirstName = obsysLabel("FIRST NAME", 35, 112, "hint");

        TextField txtLastName = obsysTextField(15, 155, 190);
        txtLastName.disableProperty().bind(
                newAccountModel.newCustomerFieldsDisabledProperty());
        txtLastName.textProperty().bindBidirectional(
                newAccountModel.newLastNameProperty());
        Label lblLastName = obsysLabel("LAST NAME", 35, 157, "hint");

        TextField txtAddress = obsysTextField(15, 200, 190);
        txtAddress.disableProperty().bind(
                newAccountModel.newCustomerFieldsDisabledProperty());
        txtAddress.textProperty().bindBidirectional(
                newAccountModel.newAddressProperty());
        Label lblAddress = obsysLabel("ADDRESS", 35, 202, "hint");

        TextField txtCity = obsysTextField(15, 245, 190);
        txtCity.disableProperty().bind(
                newAccountModel.newCustomerFieldsDisabledProperty());
        txtCity.textProperty().bindBidirectional(
                newAccountModel.newCityProperty());
        Label lblCity = obsysLabel("CITY", 35, 247, "hint");

        TextField txtPostal = obsysTextField(105, 290, 100);
        txtPostal.disableProperty().bind(
                newAccountModel.newCustomerFieldsDisabledProperty());
        txtPostal.textProperty().bindBidirectional(
                newAccountModel.newPostalProperty());
        Label lblPostal = obsysLabel("POSTAL", 125, 292, "hint");

        TextField txtPhone = obsysTextField(15, 335, 190);
        txtPhone.disableProperty().bind(
                newAccountModel.newCustomerFieldsDisabledProperty());
        txtPhone.textProperty().bindBidirectional(
                newAccountModel.newPhoneProperty());
        Label lblPhone = obsysLabel("PHONE", 35, 337, "hint");

        TextField txtEmail = obsysTextField(15, 380, 190);
        txtEmail.disableProperty().bind(
                newAccountModel.newCustomerFieldsDisabledProperty());
        txtEmail.textProperty().bindBidirectional(
                newAccountModel.newEmailProperty());
        Label lblEmail = obsysLabel("EMAIL", 35, 382, "hint");

        return new ArrayList<>(
                List.of(txtFirstName, lblFirstName, txtLastName, lblLastName,
                        txtAddress, lblAddress, txtCity, lblCity, txtPostal,
                        lblPostal, txtPhone, lblPhone, txtEmail, lblEmail)) {{
            addAll(createStateDropDown());
        }};
    }

    private ArrayList<Node> createInfoFields() {
        TextField txtFirstName = obsysTextField(230, 115, 220);
        txtFirstName.setDisable(true);
        txtFirstName.textProperty().bind(newAccountModel.firstNameProperty());
        Label lblFirstName = obsysLabel("FIRST NAME", 250, 117, "hint");

        TextField txtLastName = obsysTextField(230, 160, 220);
        txtLastName.setDisable(true);
        txtLastName.textProperty().bind(newAccountModel.lastNameProperty());
        Label lblLastName = obsysLabel("LAST NAME", 250, 162, "hint");

        TextArea txtAddress = obsysTextArea(465, 115, 220);
        txtAddress.setDisable(true);
        txtAddress.setPrefHeight(88);
        txtAddress.textProperty().bind(newAccountModel.addressProperty());
        Label lblAddress = obsysLabel("ADDRESS", 485, 117, "hint");

        TextField txtPhone = obsysTextField(465, 250, 220);
        txtPhone.setDisable(true);
        txtPhone.textProperty().bind(newAccountModel.phoneProperty());
        Label lblPhone = obsysLabel("PHONE", 485, 252, "hint");

        TextField txtEmail = obsysTextField(230, 205, 455);
        txtEmail.setDisable(true);
        txtEmail.textProperty().bind(newAccountModel.emailProperty());
        Label lblEmail = obsysLabel("EMAIL", 250, 207, "hint");

        return new ArrayList<>(
                List.of(txtFirstName, lblFirstName, txtLastName, lblLastName,
                        txtAddress, lblAddress, txtPhone, lblPhone, txtEmail,
                        lblEmail));
    }

    private ArrayList<Node> createAccountFields() {
        // Force the user into positive double values
        Pattern pattern = Pattern.compile("\\d*|\\d+\\.\\d{0,2}");

        TextField txtAcctNum = obsysTextField(465, 305, 220);
        txtAcctNum.setDisable(true);
        txtAcctNum.textProperty().bind(newAccountModel.acctNumProperty());
        Label lblAcctNum = obsysLabel("ACCOUNT NUMBER", 485, 307, "hint");

        TextField txtBalance = obsysTextField(230, 350, 220);
        txtBalance.setAlignment(Pos.CENTER);
        TextFormatter<Object> balanceFormatter = new TextFormatter<>(change ->
                pattern.matcher(
                        change.getControlNewText()).matches() ? change : null);
        txtBalance.setTextFormatter(balanceFormatter);
        txtBalance.textProperty().bindBidirectional(
                newAccountModel.balanceProperty());
        Label lblLoanAmt = obsysLabel("LOAN AMOUNT", 250, 352, "hint");
        lblLoanAmt.textProperty().bind(newAccountModel.balanceTypeProperty());

        TextField txtPayment = obsysTextField(465, 350, 220);
        txtPayment.setAlignment(Pos.CENTER);
        txtPayment.setDisable(true);
        txtPayment.visibleProperty().bind(
                newAccountModel.loanFieldsVisibleProperty());
        txtPayment.textProperty().bind(newAccountModel.paymentAmtProperty());
        Label lblPayment = obsysLabel("PAYMENT AMOUNT", 485, 352, "hint");
        lblPayment.visibleProperty().bind(
                newAccountModel.loanFieldsVisibleProperty());

        TextField txtRate = obsysTextField(230, 395, 100);
        txtRate.setAlignment(Pos.CENTER);
        TextFormatter<Object> rateFormatter = new TextFormatter<>(change ->
                pattern.matcher(
                        change.getControlNewText()).matches() ? change : null);
        txtRate.setTextFormatter(rateFormatter);
        txtRate.visibleProperty().bind(
                newAccountModel.intRateFieldVisibleProperty());
        txtRate.textProperty().bindBidirectional(
                newAccountModel.intRateProperty());
        Label lblRate = obsysLabel("RATE", 250, 397, "hint");
        lblRate.visibleProperty().bind(
                newAccountModel.intRateFieldVisibleProperty());

        return new ArrayList<>(
                List.of(txtAcctNum, lblAcctNum, txtBalance, lblLoanAmt,
                        txtPayment, lblPayment, txtRate, lblRate)) {{
            addAll(createAccountDropDowns());
        }};
    }

    @Override
    public ArrayList<Node> buildPasswordField() {
        return null;
    }

    private ArrayList<Node> createStateDropDown() {
        ComboBox<String> cmbStates = obsysStringCombo(
                newAccountModel.getStates(), 17, 292, 87);
        cmbStates.disableProperty().bind(
                newAccountModel.newCustomerFieldsDisabledProperty());
        cmbStates.valueProperty().bindBidirectional(
                newAccountModel.newStateProperty());
        Label lblState = obsysLabel("STATE", 35, 294, "hint");

        return new ArrayList<>(List.of(cmbStates, lblState));
    }

    private ArrayList<Node> createAccountDropDowns() {
        ComboBox<String> cmbAccountTypes = obsysStringCombo(
                newAccountModel.getTypes(), 230, 307, 220);
        cmbAccountTypes.valueProperty().bindBidirectional(
                newAccountModel.acctTypeProperty());
        cmbAccountTypes.setOnAction(evt -> acctTypeSelectionHandler.run());
        Label lblType = obsysLabel("ACCOUNT TYPE", 250, 309, "hint");

        ComboBox<String> cmbTerms = obsysStringCombo(
                newAccountModel.getTerms(), 335, 395, 114);
        cmbTerms.visibleProperty().bind(
                newAccountModel.loanFieldsVisibleProperty());
        cmbTerms.valueProperty().bindBidirectional(
                newAccountModel.termProperty());
        Label lblTerm = obsysLabel("TERM (MO)", 356, 397, "hint");
        lblTerm.visibleProperty().bind(
                newAccountModel.loanFieldsVisibleProperty());

        return new ArrayList<>(
                List.of(cmbAccountTypes, lblType, cmbTerms, lblTerm));
    }
}