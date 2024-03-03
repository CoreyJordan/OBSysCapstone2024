package org.obsys.obsysapp.views;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import org.obsys.obsysapp.models.CreationModel;
import org.obsys.obsysapp.models.LoginModel;

import java.util.ArrayList;

public class AcctCreationView extends ViewBuilder implements IObsysBuilder {
    private final CreationModel creationModel;
    private final LoginModel loginModel;
    private final Runnable returnHandler;
    private final Runnable lookupHandler;
    private final Runnable createLoginHandler;

    public AcctCreationView(CreationModel creationModel, LoginModel loginModel, Runnable returnHandler,
                            Runnable lookupHandler, Runnable createLoginHandler) {
        this.creationModel = creationModel;
        this.loginModel = loginModel;
        this.returnHandler = returnHandler;
        this.lookupHandler = lookupHandler;
        this.createLoginHandler = createLoginHandler;
    }

    @Override
    public AnchorPane build() {
        AnchorPane window = super.build();
        window.getChildren().addAll(createPanels());
        window.getChildren().addAll(loadImages());
        window.getChildren().addAll(createLabels());
        window.getChildren().addAll(createButtons());
        window.getChildren().addAll(createTextFields());
        window.getChildren().addAll(buildPasswordField());

        return window;
    }

    @Override
    public ArrayList<Rectangle> createPanels() {
        ArrayList<Rectangle> panels = new ArrayList<>();
        panels.add(obsysPanel(50, 140, 310, 310));
        return panels;
    }

    @Override
    public ArrayList<Node> loadImages() {
        ArrayList<Node> images = new ArrayList<>();
        images.add(obsysImage("dolphinCreate.png", 625, 5, 235, 540));
        images.add(obsysImage("dolphinLogoTan.png", 625, 340, 235, 210));

        return images;
    }

    @Override
    public ArrayList<Node> createLabels() {
        ArrayList<Node> labels = new ArrayList<>();

        Label lblAcctNumInvalid = obsysLabel("", 365, 145, "warning");
        lblAcctNumInvalid.textProperty().bindBidirectional(creationModel.acctNumWarningProperty());
        labels.add(lblAcctNumInvalid);

        Label lblFirstNameInvalid = obsysLabel("", 365, 190, "warning");
        lblFirstNameInvalid.textProperty().bindBidirectional(creationModel.firstNameWarningProperty());
        labels.add(lblFirstNameInvalid);

        Label lblLastNameInvalid = obsysLabel("", 365, 235, "warning");
        lblLastNameInvalid.textProperty().bindBidirectional(creationModel.lastNameWarningProperty());
        labels.add(lblLastNameInvalid);

        Label lblNotFound = obsysLabel("", 365, 280, 255, "warning");
        lblNotFound.textProperty().bindBidirectional(creationModel.notFoundProperty());
        labels.add(lblNotFound);

        Label lblUsernameInvalid = obsysLabel("", 365, 319, 255, "warning");
        lblUsernameInvalid.textProperty().bindBidirectional(loginModel.invalidUsernameProperty());
        labels.add(lblUsernameInvalid);

        Label lblPasswordInvalid = obsysLabel("", 365, 369, 255, "warning");
        lblPasswordInvalid.textProperty().bindBidirectional(loginModel.invalidPasswordProperty());
        labels.add(lblPasswordInvalid);

        labels.add(obsysLabel("Link your account", 25, 50, "banner"));

        String message = "Confirm your information, then create a username & password. " +
                "Passwords must be at least 6 characters, contain at least 1 upper, 1 lower, 1 symbol, 1 number.";
        labels.add(obsysLabel(message, 50, 470, 540));

        return labels;
    }

    @Override
    public ArrayList<Node> createButtons() {
        ArrayList<Node> buttons = new ArrayList<>();

        Button btnBack = obsysButton("Return to Login", 10, 10, 180, new Image("back.png"));
        btnBack.setOnAction(evt -> returnHandler.run());
        buttons.add(btnBack);

        Button btnLookUp = obsysButton("Find account", 225, 280);
        btnLookUp.setOnAction(evt -> lookupHandler.run());
        btnLookUp.setDefaultButton(true);
        buttons.add(btnLookUp);

        Button btnRegister = obsysButton("Register", 260, 409);
        btnRegister.disableProperty().bindBidirectional(creationModel.registrationDisabledProperty());
        btnRegister.setOnAction(evt -> createLoginHandler.run());
        buttons.add(btnRegister);

        return buttons;
    }

    @Override
    public ArrayList<Node> createTextFields() {
        ArrayList<Node> fields = new ArrayList<>();

        double posX = 55;
        double hintPosX = 75;
        double posY = 145;
        double hintPosY = 147;
        double width = 300;
        double translateY = 45;

        TextField txtAcctNum = obsysTextField(posX, posY, width);
        txtAcctNum.textProperty().bindBidirectional(creationModel.acctNumProperty());
        fields.add(txtAcctNum);
        fields.add(obsysLabel("ACCOUNT NUMBER", hintPosX, hintPosY, "hint"));
        posY += translateY;
        hintPosY += translateY;

        TextField txtFirstName = obsysTextField(posX, posY, width);
        txtFirstName.textProperty().bindBidirectional(creationModel.firstNameProperty());
        fields.add(txtFirstName);
        fields.add(obsysLabel("FIRST NAME", hintPosX, hintPosY, width, "hint"));
        posY += translateY;
        hintPosY += translateY;

        TextField txtLastName = obsysTextField(posX, posY, width);
        txtLastName.textProperty().bindBidirectional(creationModel.lastNameProperty());
        fields.add(txtLastName);
        fields.add(obsysLabel("LAST NAME", hintPosX, hintPosY, "hint"));
        posY = 319;
        hintPosY = 321;

        TextField txtUsername = obsysTextField(posX, posY, width);
        txtUsername.textProperty().bindBidirectional(loginModel.usernameProperty());
        txtUsername.disableProperty().bindBidirectional(creationModel.registrationDisabledProperty());
        fields.add(txtUsername);
        fields.add(obsysLabel("USERNAME", hintPosX, hintPosY, "hint"));

        return fields;
    }

    @Override
    public ArrayList<Node> buildPasswordField() {
        ArrayList<Node> passwordNodes = new ArrayList<>();

        PasswordField txtPassword = obsysPassword(55, 364);
        txtPassword.textProperty().bindBidirectional(loginModel.passwordProperty());
        txtPassword.disableProperty().bind(creationModel.passwordDisableProperty());
        passwordNodes.add(txtPassword);

        TextField txtUnmasked = obsysTextField(txtPassword.getLayoutX(),
                txtPassword.getLayoutY(), txtPassword.getPrefWidth());
        txtUnmasked.textProperty().bind(txtPassword.textProperty());
        txtUnmasked.visibleProperty().bind(txtPassword.visibleProperty().not());
        passwordNodes.add(txtUnmasked);

        passwordNodes.add(obsysLabel("PASSWORD", 75, 364, "hint"));

        Button btnPrivacy = obsysButton(new Image("privacyOn.png"), 300, 364, "toggle");
        btnPrivacy.disableProperty().bindBidirectional(creationModel.registrationDisabledProperty());
        btnPrivacy.setOnMousePressed(evt -> {
            creationModel.setPasswordDisable(true);
            txtPassword.setVisible(false);
        });
        btnPrivacy.setOnMouseReleased(ext -> {
            creationModel.setPasswordDisable(false);
            txtPassword.setVisible(true);
            txtPassword.requestFocus();
            txtPassword.positionCaret(txtPassword.getLength());
        });
        passwordNodes.add(btnPrivacy);

        return passwordNodes;
    }
}
