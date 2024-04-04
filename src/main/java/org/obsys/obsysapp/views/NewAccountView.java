package org.obsys.obsysapp.views;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import org.obsys.obsysapp.models.NewAccountModel;

import java.util.ArrayList;
import java.util.List;

public class NewAccountView extends ViewBuilder implements IObsysBuilder {
    private final NewAccountModel newAccountModel;
    private final Runnable logoutHandler;
    private final Runnable clearFormHandler;
    private final Runnable returnHandler;
    private final Runnable newCustomerHandler;
    private final Runnable registrationHandler;


    public NewAccountView(NewAccountModel newAccountModel, Runnable logoutHandler, Runnable clearFormHandler,
                          Runnable returnHandler, Runnable newCustomerHandler, Runnable registrationHandler) {
        this.newAccountModel = newAccountModel;
        this.logoutHandler = logoutHandler;
        this.clearFormHandler = clearFormHandler;
        this.returnHandler = returnHandler;
        this.newCustomerHandler = newCustomerHandler;
        this.registrationHandler = registrationHandler;
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
            add(obsysImage("dolphinLogoBlue.png", 710, 335, 225, 205));
        }};
    }

    @Override
    public ArrayList<Node> createLabels() {
        Label lblDateTime = obsysLabel("Month day, year - H:m AM", 30, 5);
        lblDateTime.textProperty().bind(newAccountModel.dateProperty());

        Label lblErrors = obsysLabel("Test string", 230, 500, "panel-warning");
        // TODO bind text property

        String instructions = "Verify Customer info or Register New Customer. \n\n" +
                "Account number, payment amounts, and due dates will appear after creation.";
        Label lblInstructions = obsysLabel(instructions, 720, 100, 190);
        lblInstructions.setWrapText(true);

        return new ArrayList<>(List.of(lblDateTime, lblErrors, lblInstructions)) {{
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

        Button btnBack = obsysButton("Home", 10, 30, 100, new Image("back.png"));
        btnBack.setOnAction(evt -> returnHandler.run());

        Button btnRegister = obsysButton("Register", 105, 425, 100);
        btnRegister.disableProperty().bind(newAccountModel.newCustomerFieldsDisabledProperty());
        btnRegister.setOnAction(evt -> registrationHandler.run());

        Button btnOpenAcct = obsysButton("Open Account", 535, 445, 150);
        // TODO set action handler

        return new ArrayList<>(List.of(hypClear, hypLogout, hypNewCustomer, btnBack, btnRegister, btnOpenAcct));
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
        txtFirstName.disableProperty().bind(newAccountModel.newCustomerFieldsDisabledProperty());
        // TODO bind text property
        Label lblFirstName = obsysLabel("FIRST NAME", 35, 112, "hint");

        TextField txtLastName = obsysTextField(15, 155, 190);
        txtLastName.disableProperty().bind(newAccountModel.newCustomerFieldsDisabledProperty());
        // TODO bind text property
        Label lblLastName = obsysLabel("LAST NAME", 35, 157, "hint");

        TextField txtAddress = obsysTextField(15, 200, 190);
        txtAddress.disableProperty().bind(newAccountModel.newCustomerFieldsDisabledProperty());
        // TODO bind text property
        Label lblAddress = obsysLabel("ADDRESS", 35, 202, "hint");

        TextField txtCity = obsysTextField(15, 245, 190);
        txtCity.disableProperty().bind(newAccountModel.newCustomerFieldsDisabledProperty());
        // TODO bind text property
        Label lblCity = obsysLabel("CITY", 35, 247, "hint");

        TextField txtPostal = obsysTextField(105, 290, 100);
        txtPostal.disableProperty().bind(newAccountModel.newCustomerFieldsDisabledProperty());
        // TODO bind text property
        Label lblPostal = obsysLabel("POSTAL", 125, 292, "hint");

        TextField txtPhone = obsysTextField(15, 335, 190);
        txtPhone.disableProperty().bind(newAccountModel.newCustomerFieldsDisabledProperty());
        // TODO bind text property
        Label lblPhone = obsysLabel("PHONE", 35, 337, "hint");

        TextField txtEmail = obsysTextField(15, 380, 190);
        txtEmail.disableProperty().bind(newAccountModel.newCustomerFieldsDisabledProperty());
        // TODO bind text property
        Label lblEmail = obsysLabel("ADDRESS", 35, 382, "hint");

        return new ArrayList<>(List.of(txtFirstName, lblFirstName, txtLastName, lblLastName, txtAddress, lblAddress,
                txtCity, lblCity, txtPostal, lblPostal, txtPhone, lblPhone, txtEmail, lblEmail)) {{
            addAll(createStateDropDown());
        }};
    }

    private ArrayList<Node> createInfoFields() {
        TextField txtFirstName = obsysTextField(230, 115, 220);
        txtFirstName.setDisable(true);
        Label lblFirstName = obsysLabel("FIRST NAME", 250, 117, "hint");
        // TODO bind text property

        TextField txtLastName = obsysTextField(230, 160, 220);
        txtLastName.setDisable(true);
        Label lblLastName = obsysLabel("LAST NAME", 250, 162, "hint");
        // TODO bind text property

        TextArea txtAddress = obsysTextArea(465, 115, 220);
        txtAddress.setDisable(true);
        txtAddress.setPrefHeight(88);
        Label lblAddress = obsysLabel("ADDRESS", 485, 117, "hint");
        // TODO bind text property

        TextField txtPhone = obsysTextField(465, 250, 220);
        txtPhone.setDisable(true);
        Label lblPhone = obsysLabel("PHONE", 485, 252, "hint");
        // TODO bind text property

        TextField txtEmail = obsysTextField(230, 205, 455);
        txtEmail.setDisable(true);
        Label lblEmail = obsysLabel("EMAIL", 250, 207, "hint");
        // TODO bind text property

        return new ArrayList<>(List.of(txtFirstName, lblFirstName, txtLastName, lblLastName, txtAddress, lblAddress,
                txtPhone, lblPhone, txtEmail, lblEmail));
    }

    private ArrayList<Node> createAccountFields() {
        TextField txtAcctNum = obsysTextField(465, 305, 220);
        txtAcctNum.setDisable(true);
        // TODO bind bidirectionally
        Label lblAcctNum = obsysLabel("ACCOUNT NUMBER", 485, 307, "hint");

        TextField txtBalance = obsysTextField(230, 350, 220);
        // TODO bind bidirectionally
        Label lblLoanAmt = obsysLabel("LOAN AMOUNT", 250, 352, "hint");
        // TODO text to account type

        TextField txtPayment = obsysTextField(465, 350, 220);
        // TODO bind visibility
        // TODO bind bidirectionally
        Label lblPayment = obsysLabel("PAYMENT AMOUNT", 485, 352, "hint");

        TextField txtRate = obsysTextField(230, 395, 100);
        // TODO bind visibility
        // TODO bind bidirectionally
        Label lblRate = obsysLabel("RATE", 250, 397, "hint");

        TextField txtDueDate = obsysTextField(465, 395, 220);
        // TODO bind visibility
        // TODO bind bidirectionally
        Label lblDue = obsysLabel("DUE DATE", 485, 397, "hint");

        return new ArrayList<>(List.of(txtAcctNum, lblAcctNum, txtBalance, lblLoanAmt, txtPayment, lblPayment,
                txtRate, lblRate, txtDueDate, lblDue)) {{
            addAll(createAccountDropDowns());
        }};
    }

    @Override
    public ArrayList<Node> buildPasswordField() {
        return null;
    }

    private ArrayList<Node> createStateDropDown() {
        ComboBox<String> cmbStates = obsysStringCombo(newAccountModel.getStates(), 17, 292, 87);
        cmbStates.disableProperty().bind(newAccountModel.newCustomerFieldsDisabledProperty());
        // TODO bind bidirectionally
        Label lblState = obsysLabel("STATE", 35, 294, "hint");

        return new ArrayList<>(List.of(cmbStates, lblState));
    }

    private ArrayList<Node> createAccountDropDowns() {
        ComboBox<String> cmbAccountTypes = obsysStringCombo(newAccountModel.getTypes(), 230, 307, 220);
        // TODO bind bidirectionally
        Label lblType = obsysLabel("ACCOUNT TYPE", 250, 309, "hint");

        ComboBox<String> cmbTerms = obsysStringCombo(newAccountModel.getTerms(), 335, 395, 114);
        cmbTerms.setEditable(true);
        // TODO bind visibility
        // TODO bind bidirectionally
        Label lblTerm = obsysLabel("TERM", 356, 397, "hint");

        return new ArrayList<>(List.of(cmbAccountTypes, lblType, cmbTerms, lblTerm));
    }
}
